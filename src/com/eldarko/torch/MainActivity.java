package com.eldarko.torch;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    
    private void setBrightness(float val)
    {
        WindowManager.LayoutParams layout = getWindow().getAttributes();
        layout.screenBrightness = val;
        getWindow().setAttributes(layout);
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        setBrightness(1F);
    }
    
    @Override
    protected void onPause()
    {
        setBrightness(-1F);
        if(mCamera != null)
            toggleFlashLight(null);

        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void toggleFrontLight(View v)
    {
        if(getWindow().getAttributes().screenBrightness == 1F)
            setBrightness(0.1F);
        else
            setBrightness(1F);
        
        toggleFlashLight(null);
    }
    
    private Camera mCamera = null;
    
    private boolean setFlashMode(String mode)
    {
        try {
            Parameters p = mCamera.getParameters();
            p.setFlashMode(mode);
            mCamera.setParameters(p);
            return true;
        }
        catch(Throwable e)
        {
            return false;
        }
    }
    
    private void toggleFlashState(boolean val)
    {
        if(val)
        {
            if(setFlashMode(Parameters.FLASH_MODE_TORCH))
                mCamera.startPreview();
            else
                Toast.makeText(this, "Flash is not supported for you phone. Sorry.", Toast.LENGTH_LONG).show();
        }
        else
        {
            setFlashMode(Parameters.FLASH_MODE_OFF);
            try {
                mCamera.stopPreview();
            }
            catch(Throwable e) {
                // ignore it
            }
        }
    }
    
    public void toggleFlashLight(View v)
    {
        if(mCamera == null)
        {
            mCamera = Camera.open();
            toggleFlashState(true);
        }
        else
        {
            toggleFlashState(false);
            mCamera.release();
            mCamera = null;
        }
    }
}
