package com.office.oa.service;

import com.office.oa.entity.Node;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class RbacServiceTest {

    @Test
    public void selectNodeByUserId() {
        List<Node> nodes = new RbacService().selectNodeByUserId(3L);
        for (Node node : nodes) {
            System.out.println(node.getNodeName());
        }
    }
}