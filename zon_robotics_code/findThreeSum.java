// 给定一个数组，判断其中是否有不同的三个数满足a+b=c。
import java.util.*;
// clarification: Could the sum of two elements exceed the int range? If so, I’ll use long for the sum.
public class findThreeSum {
    // solution 1: there is no negative number in the arr
    public List<int[]> findThreeNumList(int[] arr){
        List<int[]> ans = new ArrayList<>();
        if(arr==null || arr.length<3) return ans;
        Arrays.sort(arr);
        int len = arr.length;
        // fix the index of c, and find if there is a+b == c
        // if there is no negative number, the index c must equal or larger than index a and b
        for(int ic = 2; ic < len; ic++){
            if(ic > 2 &&arr[ic]==arr[ic-1]) continue;
            int ia = 0; int ib = ic-1;
            while(ia < ib){
                long sum = (long)arr[ia]+arr[ib];
                if(sum < arr[ic]){
                    ia++;
                }else if(sum > arr[ic]){
                    ib--;
                }else{
                    ans.add(new int[]{arr[ia],arr[ib],arr[ic]});
                    ia++;ib--;
                    while(ia<ib && arr[ia]==arr[ia-1]){
                        ia++;
                    }
                    while(ia<ib && arr[ib]==arr[ib+1]){
                        ib--;
                    }
                }
            }
        }
        return ans;
    }
    // solution 2 - there is negative number in the array
    // public List<int[]> findThreeNumList(int[] arr){
    //     List<int[]> ans = new ArrayList<>();
    //     // find a list, for each element, element[0]+element[1]=element[2]
    //     // element[0]+element[1]-element[2] = 0;
    //     // edge case: arr is null or arr length < 3
    //     // things to consider: constraints? are there any duplicate numbers in the array
    //     if(arr==null || arr.length<3) return ans;
    //     Arrays.sort(arr);
    //     int len = arr.length;
    //     // fix the index of c, and find if there is a+b == c
    //     // if there is no negative number, the index c must equal or larger than index a and b
    //     // Set<String> find = new HashSet<>();
    //     for(int ic = 0; ic < len; ic++){
    //         if(ic > 0 && arr[ic]==arr[ic-1]) continue;
    //         int ia = 0; int ib = len-1;
    //         while(ia < ib){
    //             if(ia == ic) ia++;
    //             if(ib == ic) ib--;
    //              if(ia >= ib) break;
    //             long sum = (long)arr[ia]+arr[ib];
    //             if(sum < arr[ic]){
    //                 ia++;
    //             }else if(sum > arr[ic]){
    //                 ib--;
    //             }else{
    //                 ans.add(new int[]{arr[ia],arr[ib],arr[ic]});
    //                 ia++;ib--;
    //                 while(ia<ib && arr[ia]==arr[ia-1]){
    //                     ia++;
    //                 }
    //                 while(ia<ib && arr[ib]==arr[ib+1]){
    //                     ib--;
    //                 }
    //             }
    //         }
    //     }
    //     return ans;
    // }
    // wrong solution -> cant repeat 3 sum solution
    // public List<int[]> findThreeNumList(int[] arr){
    //     List<int[]> ans = new ArrayList<>();
    //     // find a list, for each element, element[0]+element[1]=element[2]
    //     // element[0]+element[1]-element[2] = 0;
    //     // edge case: arr is null or arr length < 3
    //     // things to consider: constraints? are there any duplicate numbers in the array
    //     if(arr==null || arr.length<3) return ans;
    //     Arrays.sort(arr);
    //     int len = arr.length;
    //     for(int i = 0; i < len-2; i++){
    //         if(i > 0 && arr[i] == arr[i-1]) continue;
    //         int j = i+1; int k = len-1;
    //         if(arr[i]+arr[j]>arr[k]) break;
    //         while(j < k){
    //             int sum = arr[i]+arr[j]-arr[k];
    //             if(sum < 0){
    //                 j++;
    //             }else if(sum > 0){
    //                 break;
    //             }else{
    //                 ans.add(new int[]{arr[i],arr[j],arr[k]});
    //                 j++; k--;
    //                 while(j < k && arr[j]==arr[j-1]){
    //                     j++;
    //                 }
    //                 while(j < k && arr[k] == arr[k+1]){
    //                     k--;
    //                 }
    //             }
    //         }
    //     }
    //     return ans;
    // }
    public static void main(String[] args) {
        findThreeSum solution = new findThreeSum();
        // List<int[]> ans = solution.findThreeNumList(new int[]{1,2,3,4,5});
                // List<int[]> ans = solution.findThreeNumList(new int[]{-3,1,-2});
        // List<int[]> ans = solution.findThreeNumList(new int[]{0,0,0});
        List<int[]> ans = solution.findThreeNumList(new int[]{1,1,2,2});
        for(int[] list: ans){
            for(int num: list){
                System.out.print(num);  System.out.print(","); 
            }
            System.out.println();
        }
    }
}
