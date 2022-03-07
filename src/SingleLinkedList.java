
public class SingleLinkedList {
	public class Node {
		private Object data;
		private Node link;

		public Node(Object dataToAdd) {
			data = dataToAdd;
			link = null;
		}

		public Object getData() {
			return data;
		}

		public void setData(Object data) {
			this.data = data;
		}

		public Node getLink() {
			return link;
		}

		public void setLink(Node link) {
			this.link = link;
		}
	}

	private Node head;

	public void addLast(Object dataToAdd) {
		if (head == null) {
			Node newnode = new Node(dataToAdd);
			head = newnode;
		} else {
			Node temp = head;
			while (temp.getLink() != null)
				temp = temp.getLink();
			Node newnode = new Node(dataToAdd);
			temp.setLink(newnode);
		}
	}

	public void display() {
		if (head == null) {
			System.out.println("Linked list is empty...");
		} else {
			Node temp = head;
			while (temp != null) {
				System.out.print(temp.getData() + " ");
				temp = temp.getLink();
			}
		}
	}

	public void addFront(Object dataToAdd) {
		Node newnode = new Node(dataToAdd);
		if (head == null)
			head = newnode;
		else {
			newnode.setLink(head);
			head = newnode;
		}
	}

	public void addAlphebetically(Object dataToAdd) {
		if (head == null) {
			Node newnode = new Node(dataToAdd);
			head = newnode;
		} else {
			Node temp = head;
			if (((String) dataToAdd).compareTo((String) temp.getData()) <= 0) // Compared to head element
				addFront(dataToAdd);
			else {
				if (temp.getLink() != null && ((String) dataToAdd).compareTo((String) temp.getData()) <= 0) {
					Node newnode = new Node(dataToAdd);// compared second element (not to enter loop)
					newnode.setLink(temp.getLink());
					temp.setLink(newnode);
				} else {
					while (temp.getLink() != null
							&& ((String) dataToAdd).compareTo((String) temp.getLink().getData()) > 0) {
						temp = temp.getLink(); // datatoADD compares to next element
					}
					Node newnode = new Node(dataToAdd);
					newnode.setLink(temp.getLink());
					temp.setLink(newnode);
				}
			}
		}
	}

	public Node getHead() {
		return head;
	}
}