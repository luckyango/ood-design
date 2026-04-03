// 某金融服务公司向 AWS 申请私有云网络部署。考虑到公司业务的敏感性，AWS 建议其部署特定类型的安全系统。

// 网络中共有n台服务器，第i台服务器的安全需求由security[i]表示，每个元素代表该服务器所需的安全等级。
// 为实现最高级别的防护，AWS 安全团队推荐在设计安全系统时遵循以下规则：

// 同一个安全组内的所有服务器，安全等级必须完全相同；
// 任意两个安全组内的服务器数量，差值不能超过 1。
// 给定整数数组security，请计算为保障网络安全所需的最少安全组数量。
// Problem: Minimum Number of Security Groups
// A financial services company is deploying its private network infrastructure on AWS. 
// Due to the sensitive nature of its business, AWS recommends implementing a specialized security system.

// The network consists of n servers. 
// The security requirement of the i-th server is given by security[i], 
// where each value represents the required security level of that server.

// To ensure the highest level of protection, the AWS security team enforces the following rules when designing the security groups:

// 📋 Requirements
// Uniform Security Level
// All servers within the same security group must have the same security level.

// Balanced Group Sizes
// The difference in the number of servers between any two security groups must be at most 1.

// 🎯 Goal
// Given the integer array security, determine the minimum number of security groups required to satisfy the above constraints.
import java.util.*;

public class checkSecureGroups {

    public int minSecurityGroups(int[] security) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int s : security) {
            freq.put(s, freq.getOrDefault(s, 0) + 1);
        }

        int res = Integer.MAX_VALUE;

        // 枚举最小组大小 x
        for (int x = 1; x <= 1000; x++) {
            int totalGroups = 0;
            boolean valid = true;

            for (int f : freq.values()) {

                // 最少需要的组数（尽量用大组 x+1）
                int groups = (f + x) / (x + 1); // ceil(f / (x+1))

                // 检查是否可行
                if (groups * x > f) {
                    valid = false;
                    break;
                }

                totalGroups += groups;
            }

            if (valid) {
                res = Math.min(res, totalGroups);
            }
        }

        return res;
    }

    public static void main(String[] args) {
        checkSecureGroups sol = new checkSecureGroups();

        int[] security1 = {1,1,1,2,2,3};
        System.out.println(sol.minSecurityGroups(security1)); // expected: 4

        int[] security2 = {1,1,1,1,1};
        System.out.println(sol.minSecurityGroups(security2)); // expected: 2 (3+2)

        int[] security3 = {1,2,3,4};
        System.out.println(sol.minSecurityGroups(security3)); // expected: 4
    }
}