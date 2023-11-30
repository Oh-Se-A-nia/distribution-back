package com.ohseania.ecotag.constant;

public enum TrashType {

    CIGARETTE("담배곽/담배꽁초", "#담배"),
    PLASTIC("플라스틱류(페트병, 우유병, 스티로폼 등)", "#플라스틱류"),
    PAPER("종이(신문지, 종이컵, 종이팩 등)", "#종이류"),
    GLASS("유리(주류 병, 식기류 등)", "#유리류"),
    CAN("캔(음료수 캔, 부탄가스 등)", "#캔류"),
    VINYL("비닐(위생팩, 과자봉지 등)", "#비닐류");

    private final String type;
    private final String sortedType;

    TrashType(String type, String sortedType) {
        this.type = type;
        this.sortedType = sortedType;
    }

    public static String getSortedTypeByType(String inputType) {
        for (TrashType trashType : TrashType.values()) {
            if (trashType.type.equals(inputType)) {
                return trashType.sortedType;
            }
        }

        return null;
    }

}
