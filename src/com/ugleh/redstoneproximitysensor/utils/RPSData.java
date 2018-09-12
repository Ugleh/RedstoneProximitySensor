package com.ugleh.redstoneproximitysensor.utils;

import java.util.UUID;

public class RPSData{  

	public UUID ownerID;
	public UUID rpsID;
	public RPSLocation location;
	
	public RPSData(UUID ownerID, UUID rpsID, RPSLocation location){  
        this.ownerID=ownerID;  
        this.rpsID=rpsID;  
        this.location=location;  
 }
}