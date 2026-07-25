import java.util.*;
public class coinChange {
    // 输入现有的各种面额的钞票数量和需要找零的金额，返回钞票总张数最少的找零方式。
    // 追加问题是要追踪剩余钞票数量的变化，以及应对无法找零的情况。
    // version 1
    int[] coins = {1,5,20,50};
    int[] quantity = {50,20,2,1};

    public int getChangeNum(int amount){
        if (amount < 0) return -1;
        if(amount == 0) return 0;
        // dp[i][amount] - min number of coins to make up amount using the fisrt i coins
        // initial - infinity
        int infinity = amount+1;
        int[][] dp = new int[coins.length+1][amount+1];
        // when making up the amount, the numbers of coin i
        int[][] coinComb = new int[coins.length+1][amount+1]; 
        for(int i = 0; i <= coins.length; i++){
            Arrays.fill(dp[i], infinity);
        }
        dp[0][0] = 0;
        for(int i = 1; i <= coins.length; i++){
            // current coin and its quantity
            int val = coins[i-1]; int num = quantity[i-1];
            for(int j = 0; j <= amount; j++){
                int curUse = Math.min(num, j/val);
                for(int k = 0; k <=curUse; k++){
                    int prevAmount = j-val*k;
                    if(dp[i-1][prevAmount] == infinity) continue;
                    if(dp[i-1][prevAmount] + k < dp[i][j]){
                        dp[i][j] = dp[i-1][prevAmount]+k;
                        coinComb[i][j] = k;
                    }
                }
            }
        }
        if (dp[coins.length][amount] == infinity) return -1;
        Map<Integer,Integer> bestComb = getChangeComb(amount, coinComb);
       return dp[coins.length][amount] == infinity ? -1 : dp[coins.length][amount];
    }
    public  Map<Integer,Integer> getChangeComb(int amount, int[][] coinComb){
        Map<Integer,Integer> ans = new HashMap<>();
        for(int i = coins.length; i>=1; i--){
            int num = coinComb[i][amount];
            if(num > 0) ans.put(coins[i-1],num);
            amount -= num*coins[i-1];
        }
        return ans;
    }
    public static void main(String[] args) {
        coinChange solution = new coinChange();

        int[] testAmounts = {
            0,
            1,
            6,
            21,
            40,
            50,
            76,
            111,
            200
        };

        for (int amount : testAmounts) {
            int result = solution.getChangeNum(amount);

            if (result == -1) {
                System.out.println(
                        "Amount " + amount
                                + ": cannot make exact change"
                );
            } else {
                System.out.println(
                        "Amount " + amount
                                + ": minimum coins = " + result
                );
            }
        }
    }
}
// // 如果是ood?
// import java.util.*;

// public class CoinChangeMachine {

//     private final int[] coins;
//     private final int[] quantity;

//     public CoinChangeMachine(int[] coins, int[] quantity) {
//         if (coins == null
//                 || quantity == null
//                 || coins.length != quantity.length) {
//             throw new IllegalArgumentException("Invalid input");
//         }

//         this.coins = coins.clone();
//         this.quantity = quantity.clone();
//     }

//     // 返回具体找零方案
//     // empty map 表示无法找零
//     public Map<Integer, Integer> makeChange(int amount) {
//         Map<Integer, Integer> emptyResult = new LinkedHashMap<>();

//         if (amount < 0) {
//             return emptyResult;
//         }

//         if (amount == 0) {
//             return emptyResult;
//         }

//         int n = coins.length;
//         int infinity = amount + 1;

//         int[][] dp = new int[n + 1][amount + 1];

//         // choice[i][j]:
//         // 组成 j 的最优方案中，第 i 种钞票用了多少张
//         int[][] choice = new int[n + 1][amount + 1];

//         for (int i = 0; i <= n; i++) {
//             Arrays.fill(dp[i], infinity);
//         }

//         dp[0][0] = 0;

//         for (int i = 1; i <= n; i++) {
//             int value = coins[i - 1];
//             int available = quantity[i - 1];

//             for (int currentAmount = 0;
//                  currentAmount <= amount;
//                  currentAmount++) {

//                 int maxUse = Math.min(
//                         available,
//                         currentAmount / value
//                 );

//                 for (int use = 0; use <= maxUse; use++) {
//                     int previousAmount =
//                             currentAmount - use * value;

//                     if (dp[i - 1][previousAmount] == infinity) {
//                         continue;
//                     }

//                     int candidate =
//                             dp[i - 1][previousAmount] + use;

//                     if (candidate < dp[i][currentAmount]) {
//                         dp[i][currentAmount] = candidate;
//                         choice[i][currentAmount] = use;
//                     }
//                 }
//             }
//         }

//         // 无法准确找零，库存不变
//         if (dp[n][amount] == infinity) {
//             return emptyResult;
//         }

//         Map<Integer, Integer> combination =
//                 getChangeCombination(amount, choice);

//         // 找零成功后再扣库存
//         updateInventory(combination);

//         return combination;
//     }

//     private Map<Integer, Integer> getChangeCombination(
//             int amount,
//             int[][] choice) {

//         Map<Integer, Integer> result =
//                 new LinkedHashMap<>();

//         int remaining = amount;

//         for (int i = coins.length; i >= 1; i--) {
//             int used = choice[i][remaining];
//             int denomination = coins[i - 1];

//             if (used > 0) {
//                 result.put(denomination, used);
//             }

//             remaining -= denomination * used;
//         }

//         return result;
//     }

//     private void updateInventory(
//             Map<Integer, Integer> combination) {

//         for (int i = 0; i < coins.length; i++) {
//             int used =
//                     combination.getOrDefault(coins[i], 0);

//             quantity[i] -= used;
//         }
//     }

//     public Map<Integer, Integer> getInventory() {
//         Map<Integer, Integer> inventory =
//                 new LinkedHashMap<>();

//         for (int i = 0; i < coins.length; i++) {
//             inventory.put(coins[i], quantity[i]);
//         }

//         return inventory;
//     }

//     public static void main(String[] args) {
//         CoinChangeMachine machine =
//                 new CoinChangeMachine(
//                         new int[]{1, 5, 20, 50},
//                         new int[]{50, 20, 2, 1}
//                 );

//         System.out.println(
//                 "Initial inventory: "
//                         + machine.getInventory()
//         );

//         Map<Integer, Integer> first =
//                 machine.makeChange(76);

//         if (first.isEmpty()) {
//             System.out.println("Cannot make change");
//         } else {
//             System.out.println("Change: " + first);
//         }

//         System.out.println(
//                 "Remaining inventory: "
//                         + machine.getInventory()
//         );

//         Map<Integer, Integer> second =
//                 machine.makeChange(200);

//         if (second.isEmpty()) {
//             System.out.println("Cannot make change for 200");
//         } else {
//             System.out.println("Change: " + second);
//         }

//         System.out.println(
//                 "Final inventory: "
//                         + machine.getInventory()
//         );
//     }
// }