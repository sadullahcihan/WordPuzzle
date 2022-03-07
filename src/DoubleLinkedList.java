public class DoubleLinkedList {
	public class Node {
		private Object data;
		private Node previous;
		private Node next;

		public Node(Object data) {
			this.data = data;
			previous = null;
			next = null;
		}

		public Object getData() {
			return data;
		}

		public void setData(Object data) {
			this.data = data;
		}

		public Node getPrevious() {
			return previous;
		}

		public void setPrevious(Node previous) {
			this.previous = previous;
		}

		public Node getNext() {
			return next;
		}

		public void setNext(Node next) {
			this.next = next;
		}
	}

	private Node head;
	private Node tail;

	public DoubleLinkedList() {
		head = null;
		tail = null;
	}

	public void addToTheEnd(Object dataToAdd) {
		Node newNode = new Node(dataToAdd);
		if (head == null && tail == null)
			head = tail = newNode;
		else {
			newNode.setPrevious(tail);
			tail.setNext(newNode);
			tail = newNode;
		}
	}

	public void displayFromHead() {
		Node temp = head;
		while (temp != null) {
			System.out.print(temp.getData() + " ");
			temp = temp.getNext();
		}
		System.out.println();
	}

	public void displayFromTail() {
		Node temp = tail;
		while (temp != null) {
			System.out.print(temp.getData() + " ");
			temp = temp.getPrevious();
		}
		System.out.println();
	}

	public int size() {
		Node temp = head;
		int size = 0;
		if (temp == null)
			return size;
		else {
			while (temp != null) {
				size++;
				temp = temp.getNext();
			}
			return size;
		}
	}

	public void addBetween(Object dataToAdd) {
		Node newNode = new Node(dataToAdd);
		if (head == null && tail == null)
			head = tail = newNode;
		else {
			if (Integer.parseInt(((String) dataToAdd).split(";")[1]) >= Integer
					.parseInt(((String) head.getData()).split(";")[1])) { // the best player ever case
				newNode.setNext(head);
				head.setPrevious(newNode);
				head = newNode;
			} else {
				Node temp = head;
				while (temp.getNext() != null && Integer.parseInt(((String) dataToAdd).split(";")[1]) < Integer
						.parseInt(((String) temp.getData()).split(";")[1])) {
					temp = temp.getNext();
				}
				if (temp.getNext() == null && Integer.parseInt(((String) dataToAdd).split(";")[1]) < Integer
						.parseInt(((String) temp.getData()).split(";")[1])) { // adds last
					temp.setNext(newNode);
					newNode.setPrevious(temp);
					tail = newNode;
				} else { // adds between
					newNode.setPrevious(temp.getPrevious());
					newNode.setNext(temp);
					temp.getPrevious().setNext(newNode);
					temp.setPrevious(newNode);
				}
			}
		}
	}

	public Node getHead() {
		return head;
	}

	public void setHead(Node head) {
		this.head = head;
	}

	public Node getTail() {
		return tail;
	}

	public void setTail(Node tail) {
		this.tail = tail;
	}
}