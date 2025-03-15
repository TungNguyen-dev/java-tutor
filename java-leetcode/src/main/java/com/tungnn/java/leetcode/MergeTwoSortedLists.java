package com.tungnn.java.leetcode;

public class MergeTwoSortedLists {

    public static void main(String[] args) {
        ListNode l1 = new ListNode(1);
        l1.next = new ListNode(2);
        l1.next.next = new ListNode(4);

        ListNode l2 = new ListNode(1);
        l2.next = new ListNode(3);
        l2.next.next = new ListNode(4);

        ListNode result = mergeTwoLists(l1, l2);
        System.out.println(result);
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    public static ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode mergedList;

        if (list1 != null && list2 != null) {
            if (list1.val < list2.val) {
                mergedList = new ListNode(list1.val);
                list1 = list1.next;
            } else {
                mergedList = new ListNode(list2.val);
                list2 = list2.next;
            }
        } else if (list1 != null) {
            mergedList = new ListNode(list1.val);
            list1 = list1.next;
        } else if (list2 != null) {
            mergedList = new ListNode(list2.val);
            list2 = list2.next;
        } else {
            return null;
        }

        ListNode head = mergedList;

        while (list1 != null && list2 != null) {
            int val1 = list1.val;
            int val2 = list2.val;

            if (val1 <= val2) {
                mergedList.next = list1;
                list1 = list1.next;
            } else {
                mergedList.next = list2;
                list2 = list2.next;
            }

            mergedList = mergedList.next;
        }

        if (list1 != null) {
            mergedList.next = list1;
        } else if (list2 != null) {
            mergedList.next = list2;
        }

        return head;
    }
}
