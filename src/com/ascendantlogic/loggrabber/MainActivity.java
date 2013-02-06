package com.ascendantlogic.loggrabber;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.zip.GZIPOutputStream;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    TextView appLogStatus;
    TextView systemLogStatus;
    TextView radioLogStatus;
    TextView eventLogStatus;
    TextView saveLocation;
    
    Button saveLogsButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        appLogStatus = (TextView)findViewById(R.id.appLogStatus);
        systemLogStatus = (TextView)findViewById(R.id.systemLogStatus);
        radioLogStatus = (TextView)findViewById(R.id.radioLogStatus);
        eventLogStatus = (TextView)findViewById(R.id.eventLogStatus);
        saveLocation = (TextView)findViewById(R.id.saveLocation);
        
        saveLogsButton = (Button)findViewById(R.id.saveLogsButton);
        saveLogsButton.setOnClickListener(saveLogsClickListener);
    }

    private Button.OnClickListener saveLogsClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            String currentTime = String.valueOf(System.currentTimeMillis());
            
            appLogStatus.setText("");
            systemLogStatus.setText("");
            radioLogStatus.setText("");
            eventLogStatus.setText("");
            saveLocation.setText("");
            
            String snapshotFilename = "app-" + currentTime + ".gz";
            String systemFilename = "system-" + currentTime + ".gz";
            String radioFilename = "radio-" + currentTime + ".gz";
            String eventsFilename = "event-" + currentTime + ".gz";
            
            try {
                writeLogToFile("logcat -d", snapshotFilename);
                appLogStatus.setText("Successful");
            } catch (Exception e) {
                appLogStatus.setText("Error");
                Log.e("LogcatTest", getStackTrace(e));
            }
            
            try {
                writeLogToFile("logcat -d -b system", systemFilename);
                systemLogStatus.setText("Successful");
            } catch (Exception e) {
                systemLogStatus.setText("Error");
                Log.e("LogcatTest", getStackTrace(e));
            }

            try {
                writeLogToFile("logcat -d -b radio", radioFilename);
                radioLogStatus.setText("Successful");
            } catch (Exception e) {
                radioLogStatus.setText("Error");
                Log.e("LogcatTest", getStackTrace(e));
            }

            try {
                writeLogToFile("logcat -d -b event", eventsFilename);
                eventLogStatus.setText("Successful");
            } catch (Exception e) {
                eventLogStatus.setText("Error");
                Log.e("LogcatTest", getStackTrace(e));
            }

        }
    };
    
    private void writeLogToFile(String logCommand, String filename) throws IOException {
        File externalStorageDir = null;
        GZIPOutputStream zipFileStream = null;
        InputStream logcatInputStream = null;
        byte[] fileBuffer = new byte[4096];
        int len = 0;
        
        externalStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        
        if (externalStorageDir != null) {
            saveLocation.setText(externalStorageDir.getAbsolutePath());
            
            zipFileStream = new GZIPOutputStream(new FileOutputStream(new File(externalStorageDir.getAbsolutePath()  + "/" + filename)));
                
            logcatInputStream = Runtime.getRuntime().exec(logCommand).getInputStream();
            
            while((len = logcatInputStream.read(fileBuffer)) > 0) {
                zipFileStream.write(fileBuffer, 0, len);
            }
            
            zipFileStream.flush();
            zipFileStream.close();
        }
    }
    
    public static String getStackTrace(Throwable t) {
        if (t != null) {
            StringWriter sWriter = new StringWriter();
            PrintWriter pWriter = new PrintWriter(sWriter);
            t.printStackTrace(pWriter);

            return sWriter.getBuffer().toString();
        }
        return "";
    }
}
