/*
 * Basic no frills app which integrates the ZBar barcode scanner with
 * the camera.
 *
 * Created by lisah0 on 2012-02-24
 */
package com.sinch.messagingtutorial.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/* Import ZBar Class files */

public class CameraTestActivity extends Activity
{
    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;
    private StringBuffer output = new StringBuffer(150);
    private String[] attrNames = new String[150];
    private String[] attrValues = new String[150];
    private String name;
    private String no;
    private String dob;

    TextView scanText;
    Button scanButton;
    private String str;
    //String str1;
    //private String sStringToParse;
    ImageScanner scanner;

    private boolean barcodeScanned = false;
    private boolean previewing = true;

    static {
        System.loadLibrary("iconv");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        Intent gotintent = getIntent();
        name = gotintent.getStringExtra("txtname");
        dob = gotintent.getStringExtra("txtdob");
        no = gotintent.getStringExtra("txtno");


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

        scanText = (TextView)findViewById(R.id.scanText);

        scanButton = (Button)findViewById(R.id.ScanButton);

        scanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (barcodeScanned) {
                    barcodeScanned = false;
                    scanText.setText("Scanning...");
                    mCamera.setPreviewCallback(previewCb);
                    mCamera.startPreview();
                    previewing = true;
                    mCamera.autoFocus(autoFocusCB);
                }
            }
        });
    }

    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    PreviewCallback previewCb = new PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();

                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {
                    scanText.setText("barcode result " + sym.getData());

                    output.append(sym.getData());
                    barcodeScanned = true;





                }
                str=output.toString();
                str = str.replace("\"", "\'");
                //str1 = str.replace("\'", " ");
                String sStringToParse = new String(str);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setValidating(false);
                DocumentBuilder db = null;
                try {
                    db = dbf.newDocumentBuilder();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
                Document doc = null;
                try {
                    doc = db.parse(new ByteArrayInputStream(sStringToParse.getBytes("utf-8")));
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                NodeList nlRecords = doc.getElementsByTagName("PrintLetterBarcodeData");

                int num = nlRecords.getLength();

                for (int i = 0; i < num; i++) {
                    Element node = (Element) nlRecords.item(i);

                    //System.out.println("List attributes for node: " + node.getNodeName());

                    // get a map containing the attributes of this node
                    NamedNodeMap attributes = node.getAttributes();

                    // get the number of nodes in this map
                    int numAttrs = attributes.getLength();

                    for (int j = 0; j < numAttrs; j++) {
                        Attr attr = (Attr) attributes.item(j);

                        attrNames[j] = attr.getNodeName();
                        attrValues[j] = attr.getNodeValue();

                        // Do your stuff here
                        //System.out.println("Found attribute: " + attrName + " with value: " + attrValue);

                    }

                }
                Intent myIntent = new Intent(CameraTestActivity.this,DisplayActivity.class);
                myIntent.putExtra("myattrnames",attrNames);
                myIntent.putExtra("myattrvalues",attrValues);
                myIntent.putExtra("myname",name);
                myIntent.putExtra("mydob",dob);
                myIntent.putExtra("myno",no);
                myIntent.putExtra("parse",str);
                startActivity(myIntent);


            }



            }

        };



    // Mimic continuous auto-focusing
    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };
}


