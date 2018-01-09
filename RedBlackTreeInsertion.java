import java.util.Stack;

class RedBlackTreeInsertion {

	public enum Color {
        RED,
        BLACK
	}
	
	@Override
	public String toString() {
		
		return "RedBlackTree";
	}

	public static class Node {
		int data;
		Color color;
		Node left;
		Node right;
		Node parent;
		boolean isNullLeaf;
		@Override
		public String toString() {
			return "Data="+data+" Color="+color.toString();
		}
		
		
		
	}
	
	private static Node createNullLeafNode(Node parent) {
        Node leaf = new Node();
        leaf.color = Color.BLACK;
        leaf.isNullLeaf = true;
        leaf.parent = parent;
        return leaf;
    }

	private static Node createBlackNode(int data) {
		Node node = new Node();
		node.data = data;
		node.color = Color.BLACK;
		node.parent = null;
		node.left = createNullLeafNode(node);
		node.right = createNullLeafNode(node);
		return node;
	}
	
	private static Node createRedNode(int data,Node parent) {
		Node node = new Node();
		node.data = data;
		node.color = Color.RED;
		node.left = createNullLeafNode(parent);
		node.right = createNullLeafNode(parent);
		node.parent = parent;
		return node;
	}
	
	private static boolean isLeftChild(Node root) {
		return (root.parent != null && root.parent.left == root);
	}
	
	private static Node findSibling(Node root) {
		if (isLeftChild(root))
			return root.parent.right.isNullLeaf ? null : root.parent.right;
		else
			return root.parent.left.isNullLeaf ? null : root.parent.left;
	}
	
	private static Node leftRotate(Node root,boolean changeColor) {
		Node rootParent = root.parent;
		root.parent = rootParent.parent;
		if(rootParent.parent != null) {
			if(isLeftChild(rootParent))
				rootParent.parent.left = root;
			else
				rootParent.parent.right = root;
		}
		
		Node rootLeft = root.left;
		root.left = rootParent;
		rootParent.parent = root;
		rootParent.right = rootLeft;
		if(rootLeft != null && !rootLeft.isNullLeaf)
			rootLeft.parent = rootParent;
		
		if(changeColor) {
			root.color = Color.BLACK;
			rootParent.color = Color.RED;
		}
		return root;
	}
	
	private static Node rightRotate(Node root,boolean changeColor) {
		Node rootParent = root.parent;
		
		if(rootParent.parent != null) {
			root.parent = rootParent.parent;
			if(isLeftChild(rootParent))
				rootParent.parent.left = root;
			else
				rootParent.parent.right = root;
		}
		
		Node rootRight = root.right;
		root.right = rootParent;
		rootParent.parent = root;
		rootParent.left = rootRight;
		if(rootRight != null && !rootRight.isNullLeaf)
			rootRight.parent = rootParent;
		
		if(changeColor) {
			root.color = Color.BLACK;
			rootParent.color = Color.RED;
		}
		return root;
	}
	
	
	private Node insert(Node root, int data) {
		return insert(null,root,data);
	}
	
	private Node insert(Node parent,Node root,int data) {
		
		if(root == null || root.isNullLeaf) {
			if(parent != null) {
				return createRedNode(data,parent);
			} else 
				return createBlackNode(data);
		}
		if(root.data == data)
			return root;
		
		boolean isLeft;
		if(root.data > data) {
			Node left = insert(root,root.left,data);
			if(left == root.parent)
				return left;
			root.left = left;
			isLeft = true;
		} else {
			Node right = insert(root,root.right,data);
			if(right == root.parent)
				return right;
			root.right = right;
			isLeft = false;
		}
		
		if(isLeft) {
			if(root.color == Color.RED && root.left.color == Color.RED) {
				Node uncle = findSibling(root);
				if(uncle == null || uncle.color == Color.BLACK) {
					//Need to perform rotations
					if(isLeftChild(root)) {
						return rightRotate(root,true);
					} else {
						root = rightRotate(root.left,false);
						return leftRotate(root,true);
					}
					
				} else {
					if(uncle != null) {
						uncle.color = Color.BLACK;
					}
					root.color = Color.BLACK;
					if(root.parent.parent != null)
						root.parent.color = Color.RED;
					return root;
				}
			}
		} else {
			if(root.color == Color.RED && root.right.color == Color.RED) {
				Node uncle = findSibling(root);
				if(uncle == null || uncle.color == Color.BLACK) {
					//Need to perform rotations
					if(!isLeftChild(root)) {
						return leftRotate(root,true);
					} else {
						root = leftRotate(root.right,false);
						return rightRotate(root,true);
					}
					
				} else {
					if(uncle != null) {
						uncle.color = Color.BLACK;
					}
					root.color = Color.BLACK;
					if(root.parent.parent != null)
						root.parent.color = Color.RED;
					return root;
				}
			}
		}
		return root;
		
	}
	
	
	public static Node minValueNode(Node root) {
		Node current = root;
		while(current.left != null && !current.left.isNullLeaf) {
			current = current.left;
		}
		return current;
	}
	
	public static void printBSTInorder(Node root) {
		if (root == null) {
            return;
        }
        
        //keep the nodes in the path that are waiting to be visited
        Stack<Node> stack = new Stack<Node>();
        Node node = root;
         
        //first node to be visited will be the left one
        while (node != null) {
            stack.push(node);
            node = node.left;
        }
         
        // traverse the tree
        while (stack.size() > 0) {
           
            // visit the top node
            node = stack.pop();
            System.out.print(node.isNullLeaf ? "" : node.data + " ");
            if (node.right != null) {
                node = node.right;
                 
                // the next node to be visited is the leftmost
                while (node != null) {
                    stack.push(node);
                    node = node.left;
                }
            }
        }
        System.out.println();
    }
	

}
