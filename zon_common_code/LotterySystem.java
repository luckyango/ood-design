import java.util.*;
public class LotterySystem {
//     ##Problem:     
// # Amazon wants to build a lottery system. 
// When a customer purchases items worth between $1 and $100, 
// then they are entered into the lottery. 
// # Customers that purchase an item for $10 should be 10 times more likely to win the lottery 
// than customers who have purchased an item for $1
// # [i.e. there is a linear relationship between the amount a customer purchased an item for 
// and their probability of winning].
// #Write a function that takes a list of customers and purchase price as input.
//  As output, it should return K winners.

// pick a customer randomly
// the probability - depend on how much the user spends on the item
    // customers: {1,1} (id 1 spend 1 dollar), {2, 10}, {3,40}
    private final Random rand = new Random();
    public List<Integer> pickKWinners(List<int[]> customers, int k) {
        if(customers==null || customers.size()==0) return new ArrayList<>();
        if (k < 0) throw new IllegalArgumentException("k cannot be negative");
        int n = customers.size();
        int[] prefixSum = new int[n];// 1,11,51
        for(int i = 0; i < n; i++){
            prefixSum[i] = i==0? customers.get(i)[1]:(customers.get(i)[1]+prefixSum[i-1]);
        }
        
        Set<Integer> pickSet = new HashSet<>();
        List<Integer> ans = new ArrayList<>();
        while(pickSet.size() < customers.size()&& ans.size() < k){
            int curIdx = pickCustomer(prefixSum);
            int curCustomer = customers.get(curIdx)[0];
            if(pickSet.add(curCustomer)) ans.add(curCustomer);
        }
        return ans;
    }
    public int pickCustomer(int[] prefixSum){
        int randVal = rand.nextInt(prefixSum[prefixSum.length-1]);

        int left = 0; int right = prefixSum.length;
        while(left < right){
            int mid = left + (right-left)/2;
            if(prefixSum[mid] <= randVal){
                left = mid+1;
            }else{
                right = mid;
            }
        }
        
        return left;
    }
}
// My approach is to treat each customer’s purchase amount as their lottery weight.

// First, I build a prefix-sum array of all purchase amounts. For example, 
// if three customers spent $1, $10, and $40, the prefix sums are 1, 11, and 51.

// I then generate a random number from zero to 50. A random value of zero selects the first customer,
//  values from 1 to 10 select the second customer, and values from 11 to 50 select the third customer.
//  Therefore, the customers receive 1, 10, and 40 possible random values respectively, 
// which makes their winning probabilities proportional to how much they spent.

// I use binary search to find the first prefix sum that is greater than the generated random value, 
// which gives me the selected customer in O(log n) time.

// Since a customer should only win once, I store selected customer IDs in a hash set. 
// If I select someone who has already won, I retry. 
// I continue until I have K winners or have selected every eligible customer.

// The preprocessing takes O(n), and each selection attempt takes O(log n). 
// One limitation is that retries may become inefficient if a previously selected customer has a very large weight.
