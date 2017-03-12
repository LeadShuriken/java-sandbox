/* Hidden stub code will pass a root argument to the function below. Complete the function to solve the challenge. Hint: you may want to write one or more helper functions.  

The Node class is defined as follows:
    class Node {
        int data;
        Node left;
        Node right;
     }
*/

int[] convertIntegers(List<Integer> integers)
{
    int[] ret = new int[integers.size()];
    for (int i=0; i < ret.length; i++)
    {
        ret[i] = integers.get(i).intValue();
    }
    return ret;
}

boolean sorted(final int[] list) {
    int arraySize = list.length;
    for (int i = 0; i < arraySize - 1; i++) {
        if ((list[i]) != (list[i + 1] - 1)) {
            return false;
        }
    }
    return true;
}

void validateLeftRight(Node node,List<Integer> list)
{
    if(node.left != null){
        validateLeftRight(node.left, list);
    }

    list.add(node.data);

    if( node.right != null){
        validateLeftRight( node.right, list);
    }
}

boolean checkBST(Node root) {
    List<Integer> list = new ArrayList<>();
    validateLeftRight(root,list);

    int[] newList = convertIntegers(list);
    return sorted(newList);
}