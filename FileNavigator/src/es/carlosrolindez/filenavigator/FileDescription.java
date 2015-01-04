package es.carlosrolindez.filenavigator;

import android.os.Parcel;
import android.os.Parcelable;


public class FileDescription implements Parcelable{

	public String fileName;

	
	
	
	FileDescription (String fileName)
	{
        this.fileName = fileName;

	}
	
	public FileDescription() {
        this.fileName = "";
  
	}

	@Override
    public int describeContents() {
        return 0;
    }
	
    @Override
    public void writeToParcel(Parcel parcel, int arg1) 
    {
        parcel.writeString(fileName);
    }
     
    public static final Parcelable.Creator<FileDescription> CREATOR = new Creator<FileDescription>() {
        @Override
        public FileDescription createFromParcel(Parcel parcel) 
        {
            String fileName = parcel.readString();
                
            return new FileDescription(fileName);
        }
 
        @Override
        public FileDescription[] newArray(int size) 
        {
            return new FileDescription[size];
        }
         
    };

}
