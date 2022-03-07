
public class MultiLinkedList {
	public class ParentNode {
		private Object parentData;
		private ParentNode down;
		private ChildNode right;

		public ParentNode(Object parentData) {
			this.parentData = parentData;
			down = null;
			right = null;
		}

		public Object getParentData() {
			return parentData;
		}

		public void setParentData(Object parentData) {
			this.parentData = parentData;
		}

		public ParentNode getDown() {
			return down;
		}

		public void setDown(ParentNode down) {
			this.down = down;
		}

		public ChildNode getRight() {
			return right;
		}

		public void setRight(ChildNode right) {
			this.right = right;
		}
	}

	public class ChildNode {
		private Object childData;
		private ChildNode next;

		public ChildNode(Object childData) {
			this.childData = childData;
			next = null;
		}

		public Object getChildData() {
			return childData;
		}

		public void setChildData(Object childData) {
			this.childData = childData;
		}

		public ChildNode getNext() {
			return next;
		}

		public void setNext(ChildNode next) {
			this.next = next;
		}
	}

	private ParentNode head;

	public MultiLinkedList() {
		head = null;
	}

	public void addParent(Object dataToAdd) {
		if (head == null) {
			ParentNode newnode = new ParentNode(dataToAdd);
			head = newnode;
		} else {
			ParentNode temp = head;
			while (temp.getDown() != null)
				temp = temp.getDown();
			ParentNode newnode = new ParentNode(dataToAdd);
			temp.setDown(newnode);
		}
	}

	public void addChild(Object parent, Object dataToadd) {
		if (head == null)
			System.out.println("Add a parent before child");
		else {
			ParentNode temp = head;
			while (temp != null) {
				if (parent.equals(temp.getParentData())) {
					ChildNode temp2 = temp.getRight();
					if (temp2 == null) {
						ChildNode newnode = new ChildNode(dataToadd);
						temp.setRight(newnode);
					} else {
						while (temp2.getNext() != null)
							temp2 = temp2.getNext();
						ChildNode newnode = new ChildNode(dataToadd);
						temp2.setNext(newnode);
					}
				}
				temp = temp.getDown();
			}
		}
	}

	public void display() {
		if (head == null)
			System.out.println("Multi linked list is empty");
		else {
			ParentNode temp = head;
			while (temp != null) {
				System.out.print(temp.getParentData() + " --> ");
				ChildNode temp2 = temp.getRight();
				while (temp2 != null) {
					System.out.print(temp2.getChildData() + " ");
					temp2 = temp2.getNext();
				}
				temp = temp.getDown();
				System.out.println();
			}
		}
	}

	public void deleteChild(Object parent, Object dataToDelete) {
		if (head == null)
			System.out.println("Add a parent before child");
		else {
			ParentNode temp = head;
			while (temp != null) {
				if (parent.equals(temp.getParentData())) {
					ChildNode temp2 = temp.getRight();
					if (temp2 == null) {
						System.out.println("There is no data to delete!");
					} else if (dataToDelete.equals(((String) temp2.getChildData()).split(",")[0])) { // dataToDelete == first element
						temp.setRight(temp2.getNext());
						temp2 = null;
					} else {
						if (temp2.getNext() == null) {
							System.out.println("There is no data to delete!");
						} else {
							while (temp2.getNext() != null
									&& !dataToDelete.equals(((String) temp2.getNext().getChildData()).split(",")[0])) {
								temp2 = temp2.getNext();
							}
							if (temp2.getNext() == null) {
								System.out.println("There is no data to delete!");
							} else {
								temp2.getNext().setChildData(null);
								temp2.setNext(temp2.getNext().getNext());
							}
						}
					}
				}
				temp = temp.getDown();
			}
		}
	}

	public boolean searchWord(String inputWord) {
		boolean isExist = false;
		String ctrlWord = ""; // control word from mll_
		Object parent = inputWord.charAt(0); // head letter
		if (head == null)
			System.out.println("There is no parent like that"); // it can delete later
		else {
			ParentNode temp = head;
			while (temp != null) {
				if (parent.equals(temp.getParentData())) {
					ChildNode temp2 = temp.getRight();
					if (temp2 == null) {
						break;
					} else {
						while (temp2 != null) {
							ctrlWord = temp2.getChildData().toString().split(",")[0]; // real word
							if (ctrlWord.length() == inputWord.length()) { // same length
								for (int i = 1; i < inputWord.length(); i++) { // it starts 1 because of head letter
									if (inputWord.charAt(i) != '.' && inputWord.charAt(i) == ctrlWord.charAt(i)) {
										isExist = true; // if given letter is not equal true letter and dot break
									}
									if (inputWord.charAt(i) != '.' && inputWord.charAt(i) != ctrlWord.charAt(i)) {
										isExist = false;
										break; // It's significant. Otherwise, justification won't work correctly.
									}
								}
							}
							if (isExist) {
								break;
							}
							temp2 = temp2.getNext();
						}
					}
				}
				if (isExist) {
					break;
				}
				temp = temp.getDown();
			}
		}
		return isExist;
	}

	public String findWordTurkishMeaning(String inputWord) {
		String ctrlWord = ""; // control word from mll_
		Object parent = inputWord.charAt(0); // head letter
		if (head == null)
			System.out.println("There is no parent like that"); // it can delete later
		else {
			ParentNode temp = head;
			while (temp != null) {
				if (parent.equals(temp.getParentData())) {
					ChildNode temp2 = temp.getRight();
					if (temp2 == null) {
						break;
					} else {
						while (temp2 != null) {
							ctrlWord = temp2.getChildData().toString().split(",")[0]; // real word
							if (ctrlWord.equalsIgnoreCase(inputWord)) { // same length
								return temp2.getChildData().toString().split(",")[1];
							}
							temp2 = temp2.getNext();
						}
					}
				}
				temp = temp.getDown();
			}
		}
		return ctrlWord;
	}

	public String findRandomTurkishWord(String answer) {
		String randomWord = "";
		char parent;
		if (head == null)
			System.out.print(""); // it can delete later
		else {
			do {
				ParentNode temp = head;
				parent = (char) (int) (Math.random() * 25 + 97); // ascii_ chars a to z
				while (temp != null) {
					if (parent == (char) temp.getParentData()) {
						ChildNode temp2 = temp.getRight();
						if (temp2 == null) {
							break;
						} else {
							int r = (int) Math.random() + 3;
							while (temp2 != null && r < 5) {
								randomWord = temp2.getChildData().toString().split(",")[1]; // Turkish_meaning
								temp2 = temp2.getNext();
								r++;
							}
						}
					}
					temp = temp.getDown();
				}
			} while (randomWord.equals("") || randomWord.equalsIgnoreCase(answer));
		}
		return randomWord;
	}

	public ParentNode getHead() {
		return head;
	}
}
