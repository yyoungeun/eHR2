package com.ehr;

public enum Level {
	
	BASIC(1), SILVER(2), GOLD(3);
	
	private final int value;
	
	Level(int value){
		this.value = value;
	}
	
	//db에 insert할 때 사용
	public int intValue() {
		return value;
	}
	
	//db에서 가져올 때 사용
	public static Level valueOf(int value) {
		switch(value) {
			case 1: return BASIC;
			case 2: return SILVER;
			case 3: return GOLD;
			default: throw new AssertionError("Unknown value:"+value);
		}
	}
}
