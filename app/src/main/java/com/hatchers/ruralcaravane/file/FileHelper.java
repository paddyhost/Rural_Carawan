package com.hatchers.ruralcaravane.file;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.hatchers.ruralcaravane.file.FileType.MP3;
import static com.hatchers.ruralcaravane.file.FileType.PDF;
import static com.hatchers.ruralcaravane.file.FileType.PNG;
import static com.hatchers.ruralcaravane.file.Folders.APPROOTFOLDER;


public   class FileHelper {



    public static File createfile(String CUSTOMERFOLDER,String name,String type)
    {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(APPROOTFOLDER),CUSTOMERFOLDER);

        //Create the storage directory if it does not exist
        if (! mediaStorageDir.exists())
        {
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        //Create a media file name

        File  mediaFile = new File(mediaStorageDir.getPath() + File.separator +""+ name + "."+type);
        /*
        if (type == PNG){

        }
        else if (type == MP3){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +""+ name + ".mp3");
        }
        else if (type == PDF){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +""+ name + ".pdf");
        }
        else if (type == DOC){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +""+ name + ".doc");
        } else if (type == DOCX){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +""+ name + ".docx");
        }
        else if (type == PPT){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +""+ name + ".ppt");
        }
        else if (type == PPTX){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +""+ name + ".pptx");
        } else if (type == XLS){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +""+ name + ".xls");
        }
        else if (type == XLSX){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +""+ name + ".xlsx");
        }
        else if (type == TXT) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "" + name + ".txt");
        }
        else if(type == MP4)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "" + name + ".mp4");
        }
        else if(type == MKV)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "" + name + ".mkv");
        }
        else if(type == FLV)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "" + name + ".flv");
        }
        else if(type == AVI)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "" + name + ".avi");
        }
        else if(type == ThreeGP)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "" + name + ".3gp");
        }
        else if(type==GIF)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "" + name + ".gif");
        }
        else
        {
            return null;
        }*/

        return mediaFile;
    }

    public boolean deleteFile(File file)
    {

    return file.delete();
}

    public static boolean isFileExists(File file)
    {
        return file.exists();
    }


    public static void savePNGImage(String folder,Bitmap myBitmap,String name)
    {
        try {

            FileOutputStream fo = null;
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            File image = FileHelper.createfile(folder, name, FileType.PNG);
            try {
                fo = new FileOutputStream(image);

                fo.write(bytes.toByteArray());

            } catch (IOException e) {
                e.printStackTrace();
                // return false;

            } finally {
                if (fo != null) {
                    try {
                        fo.close();
                        //       return true;
                    } catch (IOException e) {
                        e.printStackTrace();
                        //     return false;

                    }
                }


            }

        }catch (Exception e)
        {
//            return false;

        }

    }

}
