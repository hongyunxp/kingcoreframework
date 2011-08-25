
package com.jspsmart.upload;

// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi
// Source File Name:   BlobField.java



public class BlobField
{

    private String fileField;
    private String fileName;
    private String fileNameField;
    private String isFileUpload;

    public BlobField()
    {
    }

    public String getFileField()
    {
        return fileField;
    }

    public String getFileName()
    {
        return fileName;
    }

    public String getFileNameField()
    {
        return fileNameField;
    }

    public String getIsFileUpload()
    {
        return isFileUpload;
    }

    public void setFileField(String fileField)
    {
        this.fileField = fileField;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public void setFileNameField(String fileNameField)
    {
        this.fileNameField = fileNameField;
    }

    public void setIsFileUpload(String isFileUpload)
    {
        this.isFileUpload = isFileUpload;
    }
}
