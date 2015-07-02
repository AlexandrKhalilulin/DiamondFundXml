package com.epam.ak.model;

import com.epam.ak.model.model.Gem;
import com.epam.ak.model.model.Pavilion;

import java.util.Collections;

public class Runner {
    public static void main(String[] args) {
        Pavilion pavilion = new Pavilion();
        Collections.sort(pavilion.getGems(), Gem.Weight_Order);
    }
}
