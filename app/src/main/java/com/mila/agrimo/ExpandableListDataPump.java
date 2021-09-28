package com.mila.agrimo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {


    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> Nairobi = new ArrayList<String>();
        Nairobi.add("Langata");
        Nairobi.add("Kibra");
        Nairobi.add("Kasarani");
        Nairobi.add("Dagoreti North");
        Nairobi.add("Dagoreti South");

        List<String> Mombasa = new ArrayList<String>();
        Mombasa.add("Changamwe");
        Mombasa.add("Jomvu");
        Mombasa.add("Kisauni");
        Mombasa.add("Nyali");
        Mombasa.add("Likoni");

        List<String> Kisumu = new ArrayList<String>();
        Kisumu.add("Kisumu East");
        Kisumu.add("Kisumu Central");
        Kisumu.add("Kisumu West");
        Kisumu.add("Kisumu Nyakach");
        Kisumu.add("Nyando");

        List<String> Nakuru = new ArrayList<String>();
        Nakuru.add("Nakuru Town East");
        Nakuru.add("Nakuru Town West");
        Nakuru.add("Njoro");
        Nakuru.add("Molo");



        expandableListDetail.put("NAIROBI", Nairobi);
        expandableListDetail.put("MOMBASA", Mombasa);
        expandableListDetail.put("KISUMU", Kisumu);
        expandableListDetail.put("NAKURU", Nakuru);

        return expandableListDetail;
    }

}
