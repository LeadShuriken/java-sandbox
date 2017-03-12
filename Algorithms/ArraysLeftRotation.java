public class Solution {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int k = in.nextInt();
        int a[] = new int[n];
        for(int a_i=0; a_i < n; a_i++){
            a[a_i] = in.nextInt();
        }
        
        int len = a.length - 1;
        for(int i=0; i < k; i++){
            int c = a[0];
            for(int m=0; m < len; m++){
                a[m] = a[m + 1];
            }
            a[len] = c; 
        }
        
        for (int el : a) {
            System.out.print(el + " ");
        }
    }
}