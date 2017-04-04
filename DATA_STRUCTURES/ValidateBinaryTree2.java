/* Hidden stub code will pass a root argument to the function below. Complete the function to solve the challenge. Hint: you may want to write one or more helper functions.  

The Node class is defined as follows:
    class Node {
        int data;
        Node left;
        Node right;
     }
*/

boolean checkBST(Node root) {        
    List<Integer> uniqueSet = new ArrayList<Integer>();
    validateLeftRight(root,uniqueSet); 
    Iterator<Integer> myListIterator = uniqueSet.iterator(); 
    int previousValue = -1;
    while (myListIterator.hasNext()) {
        int currentValue = myListIterator.next();
        if(currentValue > previousValue)
            previousValue = currentValue;
        else
            return false;
    }
    return true;
}

void validateLeftRight(Node node,List<Integer> list)
{
    if(node.left!=null) 
    {
        validateLeftRight(node.left,list);
    }
    list.add(node.data);

    if(node.right!=null)
    {
        validateLeftRight(node.right,list);
    }
}
