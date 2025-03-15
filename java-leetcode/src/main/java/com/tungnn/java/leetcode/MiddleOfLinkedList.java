package com.tungnn.java.leetcode;

import java.util.ArrayList;
import java.util.List;

public class MiddleOfLinkedList {

    public class ListNode {
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

    public static ListNode middleNode(ListNode head) {
        int length = 0;

        ListNode currNode = head;
        while (currNode != null) {
            currNode = currNode.next;
            length++;
        }

        int middle = length / 2;

        ListNode middleNode = head;
        for (int i = 0; i < middle; i++) {
            middleNode = middleNode.next;
        }

        return middleNode;
    }

    public static ListNode middleNode2(ListNode head) {
        List<ListNode> array = new ArrayList<>();

        int length = 0;
        while (head != null) {
            array.add(head);
            head = head.next;
            length++;
        }

        return array.get(length / 2);
    }
}
