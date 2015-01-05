package com.oklab.framework.intents;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class IntentData implements Parcelable
{
	/**************************************************************************************************************
	 * final
	 **************************************************************************************************************/
	/**************************************************************************************************************
	 * property
	 **************************************************************************************************************/
	private int m_nFloorIndex = -1;
	private int m_nSeatPosition;
	private ArrayList<Download> downList = new ArrayList<Download>();
	
	/**************************************************************************************************************
	 * get/set
	 **************************************************************************************************************/
	public int getIndex() 
	{
		return m_nFloorIndex;
	}

	public void setIndex( int nIndex ) 
	{
		m_nFloorIndex = nIndex;
	}
	
	public int getSeatPostion() 
	{
		return m_nSeatPosition;
	}

	public void setSeatPostion( int nPosition ) 
	{
		m_nSeatPosition = nPosition;
	}
	
	public ArrayList<Download> getDownList() 
	{
		return downList;
	}

	public void setDownList( ArrayList<Download> list) 
	{
		downList = list;
	}
	/**************************************************************************************************************
	 * constructor
	 **************************************************************************************************************/
	public IntentData()
	{
	}
	
	public IntentData( Parcel in )
	{
		readFromParcel( in );
	}
	
	/**************************************************************************************************************
	 * interfaces
	 **************************************************************************************************************/
	@Override
	public int describeContents() 
	{
		return 0;
	}

	@Override
	public void writeToParcel( Parcel dest, int flags ) 
	{
		dest.writeInt( m_nFloorIndex );
		dest.writeInt(m_nSeatPosition);
		dest.writeTypedList(downList);
	}
	
	
	/**************************************************************************************************************
	 * normal method
	 **************************************************************************************************************/
	private void readFromParcel( Parcel in )
	{
		m_nFloorIndex = in.readInt();
		m_nSeatPosition = in.readInt();
		in.readTypedList(downList, Download.CREATOR);
	}
	
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() 
	{
        public IntentData createFromParcel( Parcel in ) 
        {
             return new IntentData( in );
        }
        public IntentData[] newArray( int size ) 
        {
            return new IntentData[ size ];
        }
	};
}
