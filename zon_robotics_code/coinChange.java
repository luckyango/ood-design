public class coinChange {
    // 输入现有的各种面额的钞票数量和需要找零的金额，返回钞票总张数最少的找零方式。
    // 追加问题是要追踪剩余钞票数量的变化，以及应对无法找零的情况。
    // version 1
    int[] notes = {1,5,20,50};
    int[] quantity = {50,20,2,1};
    public int getChangeNum(int amount){
        if(amount == 0) return 0;
        // dp - 
        return 0;
    }
}
