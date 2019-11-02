package com.safe.gallery.calculator.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.icu.text.SimpleDateFormat;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.safe.gallery.calculator.CameraData.AutoFitTextureView;
import com.safe.gallery.calculator.CameraData.Utils;
import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.app.MainApplication;
import com.safe.gallery.calculator.share.Share;
import com.safe.gallery.calculator.share.share_calc;

import net.objecthunter.exp4j.ExpressionBuilder;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.Semaphore;

import static android.os.Environment.DIRECTORY_PICTURES;

public class CalcActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private static SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 270);
        ORIENTATIONS.append(Surface.ROTATION_90, 180);
        ORIENTATIONS.append(Surface.ROTATION_180, 90);
        ORIENTATIONS.append(Surface.ROTATION_270, 0);
    }

    private String cameraId;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSessions;
    private CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;

//    public static final String IMAGE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "Photo Reflection"+ "/" + System.currentTimeMillis() + ".jpg";
    //File file = new File(IMAGE_PATH);

    private static final int REQUEST_CAMERA_PERMISSION = 200;

    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    private AutoFitTextureView textureView;
    private Button button;

    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    private static final int MAX_PREVIEW_WIDTH = 1920;
    private static final int MAX_PREVIEW_HEIGHT = 1080;
    private int mSensorOrientation;

    boolean mSurfaceTextureAvailable = false;
    boolean mPermissionsGranted = false;

    ImageView iv_eight;

    ImageView iv_five;

    ImageView iv_two;

    ImageView iv_dot;

    ImageView iv_divide;

    ImageView iv_nine;

    ImageView iv_six;

    ImageView iv_three;

    ImageView iv_plus_minus;

    ImageView iv_multiply;

    ImageView iv_minus;

    ImageView iv_plus;

    ImageView iv_equals;

    ImageView iv_sqrt;

    ImageView f6017O;

    EditText et_main;

    EditText tv_Display;

    TextView tv_divide;

    LinearLayout ll_delete;

    LinearLayout ll_calc;

    RelativeLayout rl_calc_layout;

    String f6024V = "";

    String f6025W = "";

    String f6026X = "";

    Boolean f6027Y = Boolean.valueOf(false);

    Editable f6028Z;

    boolean aB;
    boolean aC = false;
    Double aa;
    String ab = "";
    String ac = "";
    int ad = 0;
    int ae = 0;
    int af = 0;
    int ag = 0;
    Boolean ah = Boolean.valueOf(false);
    Boolean ai = Boolean.valueOf(false);
    Boolean aj = Boolean.valueOf(false);
    Boolean ak = Boolean.valueOf(false);
    Boolean al = Boolean.valueOf(false);
    Boolean am = Boolean.valueOf(false);
    Boolean an = Boolean.valueOf(false);
    Boolean ao = Boolean.valueOf(false);
    Boolean ap = Boolean.valueOf(false);
    Boolean aq = Boolean.valueOf(false);
    Boolean ar = Boolean.valueOf(true);
    Boolean as = Boolean.valueOf(true);
    Boolean at = Boolean.valueOf(true);
    Boolean au = Boolean.valueOf(true);
    Boolean av = Boolean.valueOf(false);
    Boolean aw = Boolean.valueOf(false);
    Boolean az = Boolean.valueOf(false);
    private AlphaAnimation click_anim;
    private String expressions = "";
    private String firststr = "";

    Boolean f6029n = Boolean.valueOf(false);

    Boolean f6030o = Boolean.valueOf(false);

    Boolean f6031p = Boolean.valueOf(false);
    private String prev = "";

    Boolean f6032q = Boolean.valueOf(false);

    Boolean f6033r = Boolean.valueOf(false);
    private Double result = Double.valueOf(0.0d);

    ImageView iv_clear;

    ImageView iv_seven;

    ImageView iv_four;

    ImageView iv_one;

    ImageView iv_zero;

    ImageView iv_percent;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_calc);

        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        for (Signature signature : info.signatures) {

            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            md.update(signature.toByteArray());
            Log.e("TAG","KeyHash:--->"+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }

        textureView = (AutoFitTextureView) findViewById(R.id.textureView);
        textureView.setSurfaceTextureListener(textureListener);
        initviews();
        initlisteners();
        check_tablet();

        if (MainApplication.getInstance().getPassword().isEmpty()) {

            showSetPasswordDialog();

            return;
        }

        click_anim = new AlphaAnimation(1.0f, 0.5f);

    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, final int width, final int height) {
            mSurfaceTextureAvailable = true;


            if (ContextCompat.checkSelfPermission(CalcActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CalcActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                return;
            } else {
                mPermissionsGranted = true;

                // Execute some code after 500 milliseconds have passed
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (mSurfaceTextureAvailable && mPermissionsGranted) {
                            openCamera(width, height);
                        }
                        //setupCameraIfPossible();
                    }
                }, 500);


            }

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            textureView.setTransform(Utils.configureTransform(width, height, imageDimension, CalcActivity.this));
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    private void setupCameraIfPossible() {
        if (mSurfaceTextureAvailable && mPermissionsGranted) {
            //openCamera();
        }
    }
    /*TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            if (null != textureView || null == imageDimension) {
                textureView.setTransform(Utils.configureTransform(width, height, imageDimension, CalcActivity.this));
            }
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };*/

    CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {

            Log.e("tag", "onOpened");
            mCameraOpenCloseLock.release();
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    private void openCamera(int width, int height) {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.e("tag", "is camera open");

        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            cameraId = manager.getCameraIdList()[1];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;

            Size largest = Collections.max(
                    Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                    new Utils.CompareSizesByArea());


            int displayRotation = getWindowManager().getDefaultDisplay().getRotation();
            //noinspection ConstantConditions
            mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            boolean swappedDimensions = false;
            switch (displayRotation) {
                case Surface.ROTATION_0:
                case Surface.ROTATION_180:
                    if (mSensorOrientation == 90 || mSensorOrientation == 270) {
                        swappedDimensions = true;
                    }
                    break;
                case Surface.ROTATION_90:
                case Surface.ROTATION_270:
                    if (mSensorOrientation == 0 || mSensorOrientation == 180) {
                        swappedDimensions = true;
                    }
                    break;
                default:
                    Log.e("tag", "Display rotation is invalid: " + displayRotation);
            }

            Point displaySize = new Point();
            getWindowManager().getDefaultDisplay().getSize(displaySize);
            int rotatedPreviewWidth = width;
            int rotatedPreviewHeight = height;
            int maxPreviewWidth = displaySize.x;
            int maxPreviewHeight = displaySize.y;

            if (swappedDimensions) {
                rotatedPreviewWidth = height;
                rotatedPreviewHeight = width;
                maxPreviewWidth = displaySize.y;
                maxPreviewHeight = displaySize.x;
            }

            if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                maxPreviewWidth = MAX_PREVIEW_WIDTH;
            }

            if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                maxPreviewHeight = MAX_PREVIEW_HEIGHT;
            }

            imageDimension = Utils.chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                    rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
                    maxPreviewHeight, largest);

            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                textureView.setAspectRatio(
                        imageDimension.getWidth(), imageDimension.getHeight());
            } else {
                textureView.setAspectRatio(
                        imageDimension.getHeight(), imageDimension.getWidth());
            }

            if (null != textureView || null == imageDimension) {
                textureView.setTransform(Utils.configureTransform(width, height, imageDimension, CalcActivity.this));
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CalcActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e("tag", "openCamera X");
    }

    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            //captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(CalcActivity.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    protected void updatePreview() {
        if (null == cameraDevice) {
            Log.e("tag", "updatePreview error, return");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            if (null != cameraCaptureSessions) {
                cameraCaptureSessions.close();
                cameraCaptureSessions = null;
            }
            if (null != cameraDevice) {
                cameraDevice.close();
                cameraDevice = null;
            }
            if (null != imageReader) {
                imageReader.close();
                imageReader = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.");
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(CalcActivity.this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }


    @Override
    protected void onPause() {
        Log.e("tag", "onPause");
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    protected void takePicture() {
        if (null == cameraDevice) {
            Log.e("tag", "cameraDevice is null");
            return;
        }
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = null;
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
            int width = 640;
            int height = 480;
            if (jpegSizes != null && 0 < jpegSizes.length) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            // Orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

            Log.e("TAG", "takePicture rotation:--> " + rotation);

            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);

            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);


                    //Toast.makeText(CalcActivity.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
                    createCameraPreview();
                }

            };

            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {

            Image image = null;
            try {
                image = reader.acquireLatestImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.capacity()];
                buffer.get(bytes);
                save(bytes);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (image != null) {
                    image.close();
                }
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        private void save(byte[] bytes) throws IOException {
            OutputStream output = null;
            try {


                Date todayDate = Calendar.getInstance().getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String todayString = formatter.format(todayDate);
                Log.e("TAG", "save:format -->" + todayString);

                File f = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES) + "/" + ".Calculator Vault" + "/");
                File file = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES) + "/" + ".Calculator Vault" + "/" + todayString + ".jpg");


                // File f = new File(Environment.getExternalStoragePublicDirectory("Calculator Vault") + "/");
                // File file = new File(Environment.getExternalStoragePublicDirectory("Calculator Vault") + "/" + todayString + ".jpg");

                if (!f.exists())
                    f.mkdirs();


                output = new FileOutputStream(file);
                output.write(bytes);
            } finally {
                if (null != output) {
                    output.close();
                }
            }
        }
    };


    class C16052 implements TextWatcher {

        final CalcActivity calcActivity;

        C16052(CalcActivity scientific_CalculatorActivity2) {
            calcActivity = scientific_CalculatorActivity2;
        }

        public void afterTextChanged(Editable editable) {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (calcActivity.tv_Display.getText().toString().equalsIgnoreCase("") || calcActivity.tv_Display.getText().toString().equalsIgnoreCase("0")) {
                calcActivity.tv_Display.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
            }
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!calcActivity.tv_Display.getText().toString().equalsIgnoreCase("")) {
                calcActivity.tv_Display.setSelection(calcActivity.tv_Display.getText().toString().length() - 1);
            }
        }
    }

    class C16063 implements TextWatcher {

        final CalcActivity calcActivity;

        C16063(CalcActivity scientific_CalculatorActivity2) {
            calcActivity = scientific_CalculatorActivity2;
        }

        public void afterTextChanged(Editable editable) {
            if (!calcActivity.et_main.getText().toString().equalsIgnoreCase("")) {
                calcActivity.et_main.setSelection(calcActivity.et_main.getText().toString().length() - 1);
            }
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!calcActivity.et_main.getText().toString().equalsIgnoreCase("")) {
                calcActivity.et_main.setSelection(calcActivity.et_main.getText().toString().length() - 1);
            }
        }
    }


    private void check_tablet() {
        f6017O = (ImageView) findViewById(R.id.iv_square_root);
        f6017O.setOnClickListener(this);
        if (share_calc.flag_expand.booleanValue()) {
            Log.e("flag_expand", "" + share_calc.flag_expand);
            share_calc.flag_expand = Boolean.valueOf(false);
        }
    }

    private void dot_operation() {
        if (et_main.getText().length() > 0) {
            ad = 0;
            if (ai.booleanValue()) {
                ah = Boolean.valueOf(false);
            }
            if (ah.booleanValue()) {
                et_main.setText("");
                ab = "";
                tv_Display.setText("0");
                ah = Boolean.valueOf(false);
            }
            char[] toCharArray = ab.toCharArray();
            for (int length = toCharArray.length - 1; length >= 0; length--) {
                if (toCharArray[length] == '.') {
                    ad++;
                }
                if (ad == 1) {
                    break;
                }
            }
            if (ad == 0 && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '.' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '+' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '-' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '/' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '*' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '%') {
                char charAt = et_main.getText().toString().charAt(et_main.getText().toString().length() - 1);
                tv_Display.setText(ab);
                Log.e("dot", "" + charAt);
                if (charAt < '0' || charAt > '9') {
                    Log.e("dot", "else" + charAt);
                    et_main.append("* 0.");
                    ab += "";
                    tv_Display.setText(ab);
                    return;
                }
                if (av.booleanValue()) {
                    ac += ".";
                }
                Log.e("dot", "if" + charAt);
                et_main.append(".");
                ab += ".";
                tv_Display.setText(ab);
            }
        }
    }

    private void initlisteners() {

        iv_zero.setOnClickListener(this);
        iv_one.setOnClickListener(this);
        iv_two.setOnClickListener(this);
        iv_three.setOnClickListener(this);
        iv_four.setOnClickListener(this);
        iv_five.setOnClickListener(this);
        iv_six.setOnClickListener(this);
        iv_seven.setOnClickListener(this);
        iv_eight.setOnClickListener(this);
        iv_nine.setOnClickListener(this);
        iv_plus.setOnClickListener(this);
        iv_minus.setOnClickListener(this);
        iv_multiply.setOnClickListener(this);
        iv_divide.setOnClickListener(this);
        iv_percent.setOnClickListener(this);
        iv_clear.setOnClickListener(this);
        iv_equals.setOnClickListener(this);
        iv_sqrt.setOnClickListener(this);
        ll_delete.setOnClickListener(this);
        iv_plus_minus.setOnClickListener(this);
        iv_dot.setOnClickListener(this);
        iv_zero.setOnTouchListener(this);
        iv_one.setOnTouchListener(this);
        iv_two.setOnTouchListener(this);
        iv_three.setOnTouchListener(this);
        iv_four.setOnTouchListener(this);
        iv_five.setOnTouchListener(this);
        iv_six.setOnTouchListener(this);
        iv_seven.setOnTouchListener(this);
        iv_eight.setOnTouchListener(this);
        iv_nine.setOnTouchListener(this);
        iv_plus.setOnTouchListener(this);
        iv_minus.setOnTouchListener(this);
        iv_multiply.setOnTouchListener(this);
        iv_divide.setOnTouchListener(this);
        iv_percent.setOnTouchListener(this);
        iv_clear.setOnTouchListener(this);
        iv_equals.setOnTouchListener(this);
        iv_sqrt.setOnTouchListener(this);
        ll_delete.setOnTouchListener(this);
        iv_plus_minus.setOnTouchListener(this);
        iv_dot.setOnTouchListener(this);
        tv_Display.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        tv_Display.setSingleLine();
        tv_Display.addTextChangedListener(new C16052(this));
        et_main.setSingleLine();
        tv_Display.setSingleLine();
        et_main.addTextChangedListener(new C16063(this));

    }

    private void initviews() {

        iv_clear = (ImageView) findViewById(R.id.iv_clear);
        iv_seven = (ImageView) findViewById(R.id.iv_seven);
        iv_four = (ImageView) findViewById(R.id.iv_four);
        iv_one = (ImageView) findViewById(R.id.iv_one);
        iv_zero = (ImageView) findViewById(R.id.iv_zero);
        iv_percent = (ImageView) findViewById(R.id.iv_percent);
        iv_eight = (ImageView) findViewById(R.id.iv_eight);
        iv_five = (ImageView) findViewById(R.id.iv_five);
        iv_two = (ImageView) findViewById(R.id.iv_two);
        iv_dot = (ImageView) findViewById(R.id.iv_dot);
        iv_divide = (ImageView) findViewById(R.id.iv_divide);
        iv_nine = (ImageView) findViewById(R.id.iv_nine);
        iv_six = (ImageView) findViewById(R.id.iv_six);
        iv_three = (ImageView) findViewById(R.id.iv_three);
        iv_plus_minus = (ImageView) findViewById(R.id.iv_plus_minus);
        iv_multiply = (ImageView) findViewById(R.id.iv_multiply);
        iv_minus = (ImageView) findViewById(R.id.iv_minus);
        iv_plus = (ImageView) findViewById(R.id.iv_plus);
        iv_equals = (ImageView) findViewById(R.id.iv_equals);
        iv_sqrt = (ImageView) findViewById(R.id.iv_sqrt);
        et_main = (EditText) findViewById(R.id.et_main);
        tv_Display = (EditText) findViewById(R.id.tv_Display);
        tv_divide = (TextView) findViewById(R.id.tv_divide);
        ll_delete = (LinearLayout) findViewById(R.id.ll_delete);
        ll_calc = (LinearLayout) findViewById(R.id.ll_calc);
        rl_calc_layout = (RelativeLayout) findViewById(R.id.rl_calc_layout);

    }

    private void mid_calculation() {

        if (f6026X.equals("/") && f6025W.equals("0")) {
            f6033r = Boolean.valueOf(true);
            tv_Display.setText("0");
            et_main.setText("Can't divide by 0");
            ab = "";
            return;
        }
        String valueOf = String.valueOf(f6028Z);
        Log.e("string", "" + valueOf);
        try {
            aa = Double.valueOf(new ExpressionBuilder(valueOf).build().evaluate());
            Log.e("result", "" + aa);
        } catch (ArithmeticException e) {
            e.printStackTrace();
        }
        if (String.valueOf(aa).contains("E")) {
            Double d = (Double) new ExtendedDoubleEvaluator().evaluate(String.valueOf(aa).replaceAll("%", "").replace("E", "*10^"));
            String valueOf2 = String.valueOf(d.doubleValue() / 100.0d);
            Log.e("new result", "" + d);
            Log.e("new result", "" + valueOf2);
        }
        aa = Double.valueOf(Double.parseDouble(new DecimalFormat(".##########################################").format(aa)));
        try {
            CharSequence charSequence = null;
            ExtendedDoubleEvaluator extendedDoubleEvaluator = new ExtendedDoubleEvaluator();
            Object obj;
            if (az.booleanValue()) {
                int i;
                int i2 = 0;
                int i3 = 0;
                for (i = 0; i < valueOf.length(); i++) {
                    if (valueOf.charAt(i) == '(') {
                        i2++;
                        Log.e("count_left_bracket", "" + i2);
                    }
                    if (valueOf.charAt(i) == ')') {
                        i3++;
                        Log.e("count_right_bracket", "" + i3);
                    }
                }
                i = i2 - i3;
                Log.e("diff", "" + i);
                charSequence = valueOf;
                int i4 = 0;
                while (i4 < i) {
                    i4++;
                    obj = charSequence + ")";
                }
                az = Boolean.valueOf(false);
            } else {
                obj = valueOf;
            }
            tv_Display.setText(charSequence);
            Log.e("Answer", aa + "");
            Double d = aa;
            Long valueOf3 = Long.valueOf(new Double(d.doubleValue()).longValue());
            Log.e("Double", d + "");
            Log.e("long", valueOf3 + "");
            Locale locale = Locale.US;
            NumberFormat instance = NumberFormat.getInstance();
            instance.setMaximumIntegerDigits(20);
            instance.setMaximumFractionDigits(20);
            instance.setGroupingUsed(false);
            ab = instance.format(aa);
            if (ab.length() > 16) {
                ab = ab.substring(0, 16);
            } else {
                tv_Display.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
            }
            if (ab.equalsIgnoreCase("-0")) {
                ab = "0";
            }
            tv_Display.setText(ab);
            et_main.setText(ab);
            f6024V = ab;
        } catch (Exception e2) {
            Log.e("TAG", "Toast");
            Toast.makeText(this, "Syntax Error", 0).show();
            et_main.setText("");
            tv_Display.setText("0");
            ab = "";
            ac = "";
            prev = "";
            firststr = "";
            aC = false;
            e2.printStackTrace();
            Log.e("Exception", e2 + "");
        }
    }

    private void operation() {
        if (et_main.getText().toString().length() != 0) {
            if (ah.booleanValue()) {
                ah = Boolean.valueOf(false);
            }
            if (!et_main.getText().toString().equals("Can't divide by 0") && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '+' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '-' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '/' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '*' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '%') {
                if (tv_Display.length() > 0) {
                    int i;
                    if (tv_Display.getText().charAt(0) == '-') {
                        Log.e("TAG", "if for loop :");
                        i = 1;
                        while (i < tv_Display.length()) {
                            if (tv_Display.getText().charAt(i) == '+' || tv_Display.getText().charAt(i) == '-' || tv_Display.getText().charAt(i) == '*' || tv_Display.getText().charAt(i) == '/') {
                                aC = false;
                                break;
                            } else {
                                aC = true;
                                i++;
                            }
                        }
                    } else {
                        Log.e("TAG", "else for loop :");
                        i = 0;
                        while (i < tv_Display.length()) {
                            if (tv_Display.getText().charAt(i) == '+' || tv_Display.getText().charAt(i) == '-' || tv_Display.getText().charAt(i) == '*' || tv_Display.getText().charAt(i) == '/') {
                                aC = false;
                                break;
                            } else {
                                aC = true;
                                i++;
                            }
                        }
                    }
                }
                String obj = tv_Display.getText().toString();
                if (obj.length() > 16) {
                    obj.substring(0, 16);
                } else {
                    tv_Display.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
                }
                if (aC) {
                    if (tv_Display.getText().charAt(0) != '-' || tv_Display.length() <= 1) {
                        Log.e("TAG", "display : " + ab);
                        Log.e("TAG", "display length : " + ab.length());
                        et_main.setText(et_main.getText().toString().substring(0, et_main.getText().toString().length() - ab.length()) + "-" + ab);
                        tv_Display.setText("-" + ab);
                        ar = Boolean.valueOf(false);
                        ab = tv_Display.getText().toString();
                    } else {
                        Log.e("TAG", "oth pos - in display");
                        obj = tv_Display.getText().toString();
                        Log.e("TAG", "s before : " + obj);
                        Log.e("TAG", "s before replacing string : " + tv_Display.getText().toString().substring(1));
                        CharSequence replace = obj.replace(tv_Display.getText().toString(), tv_Display.getText().toString().substring(1));
                        tv_Display.setText(tv_Display.getText().toString().substring(1));
                        if (f6025W.equalsIgnoreCase("")) {
                            et_main.setText(replace);
                        } else {
                            et_main.setText(firststr + replace);
                        }
                        ab = tv_Display.getText().toString();
                    }
                }
                if (f6027Y.booleanValue()) {
                    f6025W = ab;
                    Log.e("str2", f6025W);
                    return;
                }
                f6024V = ab;
                Log.e("str1", f6024V);
            }
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_clear:
                f6029n = Boolean.valueOf(false);
                et_main.setText("");
                tv_Display.setText("0");
                ab = "";
                f6025W = "";
                f6024V = "";
                f6026X = "";
                f6033r = Boolean.valueOf(false);
                f6031p = Boolean.valueOf(false);
                f6032q = Boolean.valueOf(false);
                ac = "";
                prev = "";
                firststr = "";
                f6026X = "";
                aC = false;
                return;
            case R.id.iv_divide:
                if (!et_main.getText().toString().equals("Can't divide by 0")) {
                    if (et_main.getText().length() == 0) {
                        Toast.makeText(this, "Select Number First", 0).show();
                        return;
                    }
                    Log.e("divide", et_main.getText().toString());
                    if (!f6033r.booleanValue()) {
                        f6031p = Boolean.valueOf(false);
                        f6032q = Boolean.valueOf(false);
                        if (ah.booleanValue()) {
                            et_main.setText(ab);
                            ab = aa + "";
                            tv_Display.setText(ab);
                            ah = Boolean.valueOf(false);
                        }
                        am = Boolean.valueOf(true);
                        f6030o = Boolean.valueOf(false);
                        if (av.booleanValue()) {
                            ac += "/";
                        }
                        if (et_main.length() > 0) {
                            tv_Display.setText(ab);
                            if (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '+' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '-' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '/' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '*' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '%') {
                                f6028Z = et_main.getText();
                                mid_calculation();
                                if (!et_main.getText().toString().equals("Can't divide by 0")) {
                                    et_main.append("/");
                                    f6029n = Boolean.valueOf(true);
                                    f6027Y = Boolean.valueOf(true);
                                    f6026X = "/";
                                    if (ap.booleanValue()) {
                                        ab += "/";
                                    }
                                } else {
                                    return;
                                }
                            } else if (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '+' || et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '-' || et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '*' || et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '/') {
                                et_main.setText(et_main.getText().toString().substring(0, et_main.getText().length() - 1));
                                f6028Z = et_main.getText();
                                mid_calculation();
                                if (!et_main.getText().toString().equals("Can't divide by 0")) {
                                    et_main.append("/");
                                    f6029n = Boolean.valueOf(true);
                                    f6027Y = Boolean.valueOf(true);
                                    f6026X = "/";
                                    if (ap.booleanValue()) {
                                        ab += "/";
                                    }
                                } else {
                                    return;
                                }
                            }
                            prev = et_main.getText().toString();
                            firststr = et_main.getText().toString();
                            return;
                        }
                        Toast.makeText(this, "Select Number First", 0).show();
                        return;
                    }
                    return;
                }
                return;
            case R.id.iv_dot:
                if (!f6033r.booleanValue()) {
                    try {
                        dot_operation();
                        return;
                    } catch (Exception e) {
                        return;
                    }
                }
                return;
            case R.id.iv_eight:
                f6033r = Boolean.valueOf(false);
                if (et_main.getText().toString().equalsIgnoreCase("0")) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("");
                }
                if (f6032q.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    f6032q = Boolean.valueOf(false);
                }
                if (f6031p.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    f6031p = Boolean.valueOf(false);
                }
                if (ah.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    ah = Boolean.valueOf(false);
                }
                if (aj.booleanValue() || ak.booleanValue() || am.booleanValue() || al.booleanValue() || an.booleanValue() || ao.booleanValue()) {
                    if (!f6030o.booleanValue()) {
                        ab = "";
                        f6030o = Boolean.valueOf(false);
                    }
                    aj = Boolean.valueOf(false);
                    ak = Boolean.valueOf(false);
                    am = Boolean.valueOf(false);
                    al = Boolean.valueOf(false);
                    an = Boolean.valueOf(false);
                    ao = Boolean.valueOf(false);
                    aq = Boolean.valueOf(false);
                }
                if (av.booleanValue()) {
                    ac += "8";
                }
                if (tv_Display.getText().toString().length() != 16 || f6029n.booleanValue()) {
                    et_main.append("8");
                    ab += "8";
                    tv_Display.setText(ab);
                }
                if (f6027Y.booleanValue()) {
                    f6025W = ab;
                    Log.e("str2", f6025W);
                    return;
                }
                f6024V = ab;
                Log.e("str1", f6024V);
                return;
            case R.id.iv_equals:


                if (!MainApplication.getInstance().getPassword().equals("")) {

                    String cpass = et_main.getText().toString();
                    if (MainApplication.getInstance().getPassword().equalsIgnoreCase(cpass)) {

                        String secretQuestion = MainApplication.getInstance().getSecurityQuestion();
                        String email = MainApplication.getInstance().getEmail();
                        if (TextUtils.isEmpty(secretQuestion)) {
                            startActivity(new Intent(CalcActivity.this, SecurityQuestionActivity.class).putExtra(SecurityQuestionActivity.TYPE, SecurityQuestionActivity.ADD));
                            finish();
                            return;
                        }
                        //takePicture();
                        startActivity(new Intent(CalcActivity.this, HomeActivity.class));
                        finish();
                        return;
                    } else if (cpass.equals("11223344")) {

                        startActivity(new Intent(this, SecurityQuestionActivity.class).putExtra(SecurityQuestionActivity.TYPE, SecurityQuestionActivity.FORGOT_PASS));

                    } else if (!f6033r.booleanValue()) {


                        if (MainApplication.getInstance().getPassword().length() == cpass.length()) {

                            takePicture();
                        }

                        if (!(!ah.booleanValue() || f6026X.equals("") || f6025W == null || f6025W.equalsIgnoreCase(""))) {
                            Log.e("str2equal", f6025W);
                            f6028Z = new SpannableStringBuilder(tv_Display.getText().toString() + f6026X + f6025W);
                            mid_calculation();
                        }
                        if (et_main.getText().length() <= 0) {

                            Toast.makeText(this, "Select Number First", 0).show();
                            return;

                        } else if (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '+' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '-' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '/' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '*' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '%') {

                            aj = Boolean.valueOf(false);
                            ak = Boolean.valueOf(false);
                            am = Boolean.valueOf(false);
                            al = Boolean.valueOf(false);
                            an = Boolean.valueOf(false);
                            ao = Boolean.valueOf(false);
                            f6028Z = et_main.getText();
                            ah = Boolean.valueOf(true);
                            mid_calculation();
                            ac = "";
                            return;
                        } else {
                            return;
                        }
                    }
                    return;
                }
                if (MainApplication.getInstance().getPassword().equals("")) {

                    if (et_main.getText().length() == 0) {

                        Toast.makeText(this, "Plz enter 4 digit", Toast.LENGTH_SHORT).show();

                    } else if (et_main.getText().length() > 4) {

                        Toast.makeText(this, "enter 4 digit pass", Toast.LENGTH_SHORT).show();

                    } else if (et_main.getText().length() < 4) {

                        Toast.makeText(this, "Plz enter 4 digit pass", Toast.LENGTH_SHORT).show();

                    } else {

                        Share.pass = Integer.parseInt(et_main.getText().toString());
                        Intent i = new Intent(CalcActivity.this, ConfirmCalcActivity.class);
                        startActivity(i);
                    }

                    return;
                }

                return;
            case R.id.iv_five:
                f6033r = Boolean.valueOf(false);
                if (et_main.getText().toString().equalsIgnoreCase("0")) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("");
                }
                if (f6032q.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    f6032q = Boolean.valueOf(false);
                }
                if (f6031p.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    f6031p = Boolean.valueOf(false);
                }
                if (ah.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    ah = Boolean.valueOf(false);
                }
                if (aj.booleanValue() || ak.booleanValue() || am.booleanValue() || al.booleanValue() || an.booleanValue() || ao.booleanValue()) {
                    if (!f6030o.booleanValue()) {
                        ab = "";
                        f6030o = Boolean.valueOf(false);
                    }
                    aj = Boolean.valueOf(false);
                    ak = Boolean.valueOf(false);
                    am = Boolean.valueOf(false);
                    al = Boolean.valueOf(false);
                    an = Boolean.valueOf(false);
                    ao = Boolean.valueOf(false);
                    aq = Boolean.valueOf(false);
                }
                if (av.booleanValue()) {
                    ac += "5";
                }
                if (tv_Display.getText().toString().length() != 16 || f6029n.booleanValue()) {
                    et_main.append("5");
                    ab += "5";
                    tv_Display.setText(ab);
                    f6029n = Boolean.valueOf(false);
                }
                if (f6027Y.booleanValue()) {
                    f6025W = ab;
                    Log.e("str2", f6025W);
                    return;
                }
                f6024V = ab;
                Log.e("str1", f6024V);
                return;
            case R.id.iv_four:
                f6033r = Boolean.valueOf(false);
                if (et_main.getText().toString().equalsIgnoreCase("0")) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("");
                }
                if (f6032q.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    f6032q = Boolean.valueOf(false);
                }
                if (f6031p.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    f6031p = Boolean.valueOf(false);
                }
                if (ah.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    ah = Boolean.valueOf(false);
                }
                if (aj.booleanValue() || ak.booleanValue() || am.booleanValue() || al.booleanValue() || an.booleanValue() || ao.booleanValue()) {
                    if (!f6030o.booleanValue()) {
                        ab = "";
                        f6030o = Boolean.valueOf(false);
                    }
                    aj = Boolean.valueOf(false);
                    ak = Boolean.valueOf(false);
                    am = Boolean.valueOf(false);
                    al = Boolean.valueOf(false);
                    an = Boolean.valueOf(false);
                    ao = Boolean.valueOf(false);
                    aq = Boolean.valueOf(false);
                }
                if (av.booleanValue()) {
                    ac += "4";
                }
                if (tv_Display.getText().toString().length() != 16 || f6029n.booleanValue()) {
                    et_main.append("4");
                    ab += "4";
                    tv_Display.setText(ab);
                    f6029n = Boolean.valueOf(false);
                }
                if (f6027Y.booleanValue()) {
                    f6025W = ab;
                    Log.e("str2", f6025W);
                    return;
                }
                f6024V = ab;
                Log.e("str1", f6024V);
                return;
            case R.id.iv_minus:
                if (!et_main.getText().toString().equals("Can't divide by 0")) {
                    if (et_main.getText().length() == 0) {
                        Toast.makeText(this, "Select Number First", 0).show();
                        return;
                    }
                    Log.e("minus", et_main.getText().toString());
                    if (!f6033r.booleanValue()) {
                        f6027Y = Boolean.valueOf(true);
                        f6031p = Boolean.valueOf(false);
                        f6032q = Boolean.valueOf(false);
                        if (ah.booleanValue()) {
                            et_main.setText(ab);
                            ab = aa + "";
                            tv_Display.setText(ab);
                            ah = Boolean.valueOf(false);
                        }
                        ak = Boolean.valueOf(true);
                        f6030o = Boolean.valueOf(false);
                        if (av.booleanValue()) {
                            ac += "-";
                        }
                        if (et_main.length() > 0) {
                            tv_Display.setText(ab);
                            if (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '+' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '-' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '/' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '*' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '%') {
                                f6028Z = et_main.getText();
                                mid_calculation();
                                if (!et_main.getText().toString().equals("Can't divide by 0")) {
                                    et_main.append("-");
                                    f6026X = "-";
                                    f6029n = Boolean.valueOf(true);
                                    f6027Y = Boolean.valueOf(true);
                                } else {
                                    return;
                                }
                            } else if (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '+' || et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '-' || et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '*' || et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '/') {
                                et_main.setText(et_main.getText().toString().substring(0, et_main.getText().length() - 1));
                                f6028Z = et_main.getText();
                                mid_calculation();
                                if (!et_main.getText().toString().equals("Can't divide by 0")) {
                                    et_main.append("-");
                                    f6026X = "-";
                                    f6029n = Boolean.valueOf(true);
                                    f6027Y = Boolean.valueOf(true);
                                } else {
                                    return;
                                }
                            }
                            prev = et_main.getText().toString();
                            firststr = et_main.getText().toString();
                            return;
                        }
                        Toast.makeText(this, "Select Number First", 0).show();
                        return;
                    }
                    return;
                }
                return;
            case R.id.iv_multiply:
                if (!et_main.getText().toString().equals("Can't divide by 0")) {
                    if (et_main.getText().length() == 0) {
                        Toast.makeText(this, "Select Number First", 0).show();
                        return;
                    }
                    Log.e("multiply", et_main.getText().toString());
                    if (!f6033r.booleanValue()) {
                        f6031p = Boolean.valueOf(false);
                        f6032q = Boolean.valueOf(false);
                        if (ah.booleanValue()) {
                            et_main.setText(ab);
                            ab = aa + "";
                            tv_Display.setText(ab);
                            ah = Boolean.valueOf(false);
                        }
                        al = Boolean.valueOf(true);
                        f6030o = Boolean.valueOf(false);
                        if (av.booleanValue()) {
                            ac += "*";
                        }
                        if (et_main.length() > 0) {
                            tv_Display.setText(ab);
                            if (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '+' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '-' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '/' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '*' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '%') {
                                f6028Z = et_main.getText();
                                mid_calculation();
                                if (!et_main.getText().toString().equals("Can't divide by 0")) {
                                    et_main.append("*");
                                    f6026X = "*";
                                    f6029n = Boolean.valueOf(true);
                                    f6027Y = Boolean.valueOf(true);
                                } else {
                                    return;
                                }
                            } else if (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '+' || et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '-' || et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '*' || et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '/') {
                                et_main.setText(et_main.getText().toString().substring(0, et_main.getText().length() - 1));
                                f6028Z = et_main.getText();
                                mid_calculation();
                                if (!et_main.getText().toString().equals("Can't divide by 0")) {
                                    et_main.append("*");
                                    f6026X = "*";
                                    f6029n = Boolean.valueOf(true);
                                    f6027Y = Boolean.valueOf(true);
                                } else {
                                    return;
                                }
                            }
                            prev = et_main.getText().toString();
                            firststr = et_main.getText().toString();
                            return;
                        }
                        Toast.makeText(this, "Select Number First", 0).show();
                        return;
                    }
                    return;
                }
                return;
            case R.id.iv_nine:
                f6033r = Boolean.valueOf(false);
                if (et_main.getText().toString().equalsIgnoreCase("0")) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("");
                }
                if (f6032q.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    f6032q = Boolean.valueOf(false);
                }
                if (f6031p.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    f6031p = Boolean.valueOf(false);
                }
                if (ah.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    ah = Boolean.valueOf(false);
                }
                if (aj.booleanValue() || ak.booleanValue() || am.booleanValue() || al.booleanValue() || an.booleanValue() || ao.booleanValue()) {
                    if (!f6030o.booleanValue()) {
                        ab = "";
                        f6030o = Boolean.valueOf(false);
                    }
                    aj = Boolean.valueOf(false);
                    ak = Boolean.valueOf(false);
                    am = Boolean.valueOf(false);
                    al = Boolean.valueOf(false);
                    an = Boolean.valueOf(false);
                    ao = Boolean.valueOf(false);
                    aq = Boolean.valueOf(false);
                }
                if (av.booleanValue()) {
                    ac += "9";
                }
                if (tv_Display.getText().toString().length() != 16 || f6029n.booleanValue()) {
                    f6029n = Boolean.valueOf(false);
                    et_main.append("9");
                    ab += "9";
                    tv_Display.setText(ab);
                }
                if (f6027Y.booleanValue()) {
                    f6025W = ab;
                    Log.e("str2", f6025W);
                    return;
                }
                f6024V = ab;
                Log.e("str1", f6024V);
                return;
            case R.id.iv_one:
                f6033r = Boolean.valueOf(false);
                if (et_main.getText().toString().equalsIgnoreCase("0")) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("");
                }
                if (f6031p.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    f6031p = Boolean.valueOf(false);
                }
                if (f6032q.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    f6032q = Boolean.valueOf(false);
                }
                if (ah.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    ah = Boolean.valueOf(false);
                }
                if (aj.booleanValue() || ak.booleanValue() || am.booleanValue() || al.booleanValue() || an.booleanValue() || ao.booleanValue()) {
                    if (!f6030o.booleanValue()) {
                        ab = "";
                        f6030o = Boolean.valueOf(false);
                    }
                    aj = Boolean.valueOf(false);
                    ak = Boolean.valueOf(false);
                    am = Boolean.valueOf(false);
                    al = Boolean.valueOf(false);
                    an = Boolean.valueOf(false);
                    ao = Boolean.valueOf(false);
                }
                if (av.booleanValue()) {
                    ac += "1";
                }
                if (tv_Display.getText().toString().length() != 16 || f6029n.booleanValue()) {
                    et_main.append("1");
                    ab += "1";
                    tv_Display.setText(ab);
                    f6029n = Boolean.valueOf(false);
                }
                if (f6027Y.booleanValue()) {
                    f6025W = ab;
                    Log.e("str2", f6025W);
                    return;
                }
                f6024V = ab;
                Log.e("str1", f6024V);
                return;
            case R.id.iv_percent:
                if (!f6033r.booleanValue()) {
                    try {
                        if (et_main.length() > 0) {
                            az = Boolean.valueOf(true);
                            try {
                                aa = Double.valueOf(new ExpressionBuilder(et_main.getText().toString() + "/100").build().evaluate());
                                Log.e("result", "" + aa);
                            } catch (ArithmeticException e2) {
                            }
                            f6026X = "";
                            NumberFormat instance = NumberFormat.getInstance();
                            instance.setMaximumIntegerDigits(20);
                            instance.setMaximumFractionDigits(20);
                            instance.setGroupingUsed(false);
                            String format = instance.format(aa);
                            if (format.length() > 16) {
                                format = format.substring(0, 16);
                            } else {
                                tv_Display.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
                            }
                            et_main.setText(format + "");
                            ab = format + "";
                            tv_Display.setText(format + "");
                            f6024V = format;
                            f6025W = "";
                            f6032q = Boolean.valueOf(true);
                            return;
                        }
                        Toast.makeText(this, "Select Number First", 0).show();
                        return;
                    } catch (Exception e3) {
                        Log.e("TAG", "Invalid for percentage" + e3.getMessage());
                        return;
                    }
                }
                return;
            case R.id.iv_plus:
                if (!et_main.getText().toString().equals("Can't divide by 0")) {
                    if (et_main.getText().length() == 0) {
                        Toast.makeText(this, "Select Number First", 0).show();
                        return;
                    }
                    Log.e("plus", et_main.getText().toString());
                    if (!f6033r.booleanValue()) {
                        f6031p = Boolean.valueOf(false);
                        f6032q = Boolean.valueOf(false);
                        if (ah.booleanValue()) {
                            et_main.setText(ab);
                            ab = aa + "";
                            tv_Display.setText(ab);
                            ah = Boolean.valueOf(false);
                        }
                        aj = Boolean.valueOf(true);
                        f6030o = Boolean.valueOf(false);
                        if (av.booleanValue()) {
                            ac += "+";
                        }
                        if (et_main.length() > 0) {
                            tv_Display.setText(ab);
                            if (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '+' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '-' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '/' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '*' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '%') {
                                f6028Z = et_main.getText();
                                Log.e("if", "if");
                                mid_calculation();
                                if (!et_main.getText().toString().equals("Can't divide by 0")) {
                                    et_main.append("+");
                                    f6026X = "+";
                                    f6029n = Boolean.valueOf(true);
                                    f6027Y = Boolean.valueOf(true);
                                } else {
                                    return;
                                }
                            } else if (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '+' || et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '-' || et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '*' || et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '/') {
                                et_main.setText(et_main.getText().toString().substring(0, et_main.getText().length() - 1));
                                f6028Z = et_main.getText();
                                Log.e("else", "else");
                                mid_calculation();
                                if (!et_main.getText().toString().equals("Can't divide by 0")) {
                                    et_main.append("+");
                                    f6026X = "+";
                                    f6027Y = Boolean.valueOf(true);
                                    if (tv_Display.getText().toString().length() == 16) {
                                        f6029n = Boolean.valueOf(true);
                                    }
                                } else {
                                    return;
                                }
                            }
                            prev = et_main.getText().toString();
                            firststr = et_main.getText().toString();
                            return;
                        }
                        Toast.makeText(this, "Select Number First", 0).show();
                        return;
                    }
                    return;
                }
                return;
            case R.id.iv_plus_minus:
                if (!et_main.getText().toString().equals("0") && !et_main.getText().toString().equals("Invalid Input") && !et_main.getText().toString().equals("Can't divide by 0")) {
                    try {
                        operation();
                        return;
                    } catch (Exception e4) {
                        return;
                    }
                }
                return;
            case R.id.iv_seven:
                f6033r = Boolean.valueOf(false);
                if (et_main.getText().toString().equalsIgnoreCase("0")) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("");
                }
                if (f6032q.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    f6032q = Boolean.valueOf(false);
                }
                if (f6031p.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    f6031p = Boolean.valueOf(false);
                }
                if (ah.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    ah = Boolean.valueOf(false);
                }
                if (aj.booleanValue() || ak.booleanValue() || am.booleanValue() || al.booleanValue() || an.booleanValue() || ao.booleanValue()) {
                    if (!f6030o.booleanValue()) {
                        ab = "";
                        f6030o = Boolean.valueOf(false);
                    }
                    aj = Boolean.valueOf(false);
                    ak = Boolean.valueOf(false);
                    am = Boolean.valueOf(false);
                    al = Boolean.valueOf(false);
                    an = Boolean.valueOf(false);
                    ao = Boolean.valueOf(false);
                    aq = Boolean.valueOf(false);
                }
                if (av.booleanValue()) {
                    ac += "7";
                }
                if (tv_Display.getText().toString().length() != 16 || f6029n.booleanValue()) {
                    f6029n = Boolean.valueOf(false);
                    et_main.append("7");
                    ab += "7";
                    tv_Display.setText(ab);
                }
                if (f6027Y.booleanValue()) {
                    f6025W = ab;
                    Log.e("str2", f6025W);
                    return;
                }
                f6024V = ab;
                Log.e("str1", f6024V);
                return;
            case R.id.iv_six:
                f6033r = Boolean.valueOf(false);
                if (et_main.getText().toString().equalsIgnoreCase("0")) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("");
                }
                if (f6032q.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    f6032q = Boolean.valueOf(false);
                }
                if (f6031p.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    f6031p = Boolean.valueOf(false);
                }
                if (ah.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    ah = Boolean.valueOf(false);
                }
                if (aj.booleanValue() || ak.booleanValue() || am.booleanValue() || al.booleanValue() || an.booleanValue() || ao.booleanValue()) {
                    if (!f6030o.booleanValue()) {
                        ab = "";
                        f6030o = Boolean.valueOf(false);
                    }
                    aj = Boolean.valueOf(false);
                    ak = Boolean.valueOf(false);
                    am = Boolean.valueOf(false);
                    al = Boolean.valueOf(false);
                    an = Boolean.valueOf(false);
                    ao = Boolean.valueOf(false);
                    aq = Boolean.valueOf(false);
                }
                if (av.booleanValue()) {
                    ac += "6";
                }
                if (tv_Display.getText().toString().length() != 16 || f6029n.booleanValue()) {
                    et_main.append("6");
                    ab += "6";
                    tv_Display.setText(ab);
                    f6029n = Boolean.valueOf(false);
                }
                if (f6027Y.booleanValue()) {
                    f6025W = ab;
                    Log.e("str2", f6025W);
                    return;
                }
                f6024V = ab;
                Log.e("str1", f6024V);
                return;
            case R.id.iv_sqrt:
                if (!f6033r.booleanValue()) {
                    if (et_main.length() > 0) {
                        f6026X = "";
                        sqrlEquals();
                        return;
                    }
                    Toast.makeText(this, "Select Number First", 0).show();
                    return;
                }
                return;
            case R.id.iv_square_root:
                if (!f6033r.booleanValue()) {
                    if (ae == 0) {
                        share_calc.flag_expand = Boolean.valueOf(true);
                        ae = 1;
                        return;
                    }
                    share_calc.flag_expand = Boolean.valueOf(false);
                    ae = 0;
                    return;
                }
                return;
            case R.id.iv_three:
                f6033r = Boolean.valueOf(false);
                if (et_main.getText().toString().equalsIgnoreCase("0")) {
                    et_main.setText("");
                    tv_Display.setText("");
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                }
                if (f6032q.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    f6032q = Boolean.valueOf(false);
                }
                if (f6031p.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    f6031p = Boolean.valueOf(false);
                }
                if (ah.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    ah = Boolean.valueOf(false);
                }
                if (aj.booleanValue() || ak.booleanValue() || am.booleanValue() || al.booleanValue() || an.booleanValue() || ao.booleanValue()) {
                    if (!f6030o.booleanValue()) {
                        ab = "";
                        f6030o = Boolean.valueOf(false);
                    }
                    aj = Boolean.valueOf(false);
                    ak = Boolean.valueOf(false);
                    am = Boolean.valueOf(false);
                    al = Boolean.valueOf(false);
                    an = Boolean.valueOf(false);
                    ao = Boolean.valueOf(false);
                    aq = Boolean.valueOf(false);
                }
                if (av.booleanValue()) {
                    ac += "3";
                }
                if (tv_Display.getText().toString().length() != 16 || f6029n.booleanValue()) {
                    et_main.append("3");
                    ab += "3";
                    tv_Display.setText(ab);
                    f6029n = Boolean.valueOf(false);
                }
                if (f6027Y.booleanValue()) {
                    f6025W = ab;
                    Log.e("str2", f6025W);
                    return;
                }
                f6024V = ab;
                Log.e("str1", f6024V);
                return;
            case R.id.iv_two:
                f6033r = Boolean.valueOf(false);
                if (et_main.getText().toString().equalsIgnoreCase("0")) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("");
                }
                if (f6032q.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    f6032q = Boolean.valueOf(false);
                }
                if (f6031p.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    f6031p = Boolean.valueOf(false);
                }
                if (ah.booleanValue()) {
                    et_main.setText("");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    tv_Display.setText("0");
                    ah = Boolean.valueOf(false);
                }
                if (aj.booleanValue() || ak.booleanValue() || am.booleanValue() || al.booleanValue() || an.booleanValue() || ao.booleanValue()) {
                    if (!f6030o.booleanValue()) {
                        ab = "";
                        f6030o = Boolean.valueOf(false);
                    }
                    aj = Boolean.valueOf(false);
                    ak = Boolean.valueOf(false);
                    am = Boolean.valueOf(false);
                    al = Boolean.valueOf(false);
                    an = Boolean.valueOf(false);
                    ao = Boolean.valueOf(false);
                }
                if (av.booleanValue()) {
                    ac += "2";
                }
                if (tv_Display.getText().toString().length() != 16 || f6029n.booleanValue()) {
                    et_main.append("2");
                    ab += "2";
                    tv_Display.setText(ab);
                    f6029n = Boolean.valueOf(false);
                }
                if (f6027Y.booleanValue()) {
                    f6025W = ab;
                    Log.e("str2", f6025W);
                    return;
                }
                f6024V = ab;
                Log.e("str1", f6024V);
                return;
            case R.id.iv_x_exclamation:

                if (!f6033r.booleanValue() && et_main.length() != 0) {

                    String str;
                    Boolean valueOf;
                    az = Boolean.valueOf(true);
                    Boolean.valueOf(false);
                    Log.e("string", ac);
                    if (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == ')') {
                        str = ac;
                        valueOf = Boolean.valueOf(true);
                    } else {
                        str = et_main.getText().toString();
                        valueOf = Boolean.valueOf(false);
                    }
                    String str2 = "";
                    try {
                        CharSequence charSequence;
                        CalculateFactorial calculateFactorial = new CalculateFactorial();
                        int[] factorial = calculateFactorial.factorial((int) Double.parseDouble(String.valueOf(new ExtendedDoubleEvaluator().evaluate(str))));
                        int res = calculateFactorial.getRes();
                        if (res > 20) {
                            for (int i = res - 1; i >= res - 20; i--) {
                                if (i == res - 2) {
                                    str2 = str2 + ".";
                                }
                                str2 = str2 + factorial[i];
                            }
                            charSequence = str2 + "E" + (res - 1);
                        } else {
                            charSequence = str2;
                            int i2 = res - 1;
                            while (i2 >= 0) {
                                /*String str3 = charSequence + factorial[i2];
                                i2--;
                                Object obj = str3;*/
                            }
                        }
                        if (valueOf.booleanValue()) {
                            CharSequence d = ((Double) new ExtendedDoubleEvaluator().evaluate(et_main.getText().toString().replace(ac, charSequence))).toString();
                            et_main.setText(d);
                            tv_Display.setText(d);
                            ac = "";
                            aw = Boolean.valueOf(false);
                            return;
                        }
                        et_main.setText(charSequence);
                        tv_Display.setText(charSequence);
                        return;
                    } catch (Exception e32) {
                        if (e32.toString().contains("ArrayIndexOutOfBoundsException")) {
                            et_main.setText("Result too big!");
                        } else {
                            et_main.setText("Invalid!!");
                        }
                        e32.printStackTrace();
                        return;
                    }
                }
                return;
            case R.id.iv_zero:
                if (!et_main.getText().toString().equalsIgnoreCase("0")) {
                    if (f6031p.booleanValue()) {
                        et_main.setText("");
                        ab = "";
                        f6025W = "";
                        f6024V = "";
                        f6026X = "";
                        tv_Display.setText("0");
                        f6031p = Boolean.valueOf(false);
                    }
                    if (f6032q.booleanValue()) {
                        et_main.setText("");
                        ab = "";
                        f6025W = "";
                        f6024V = "";
                        f6026X = "";
                        tv_Display.setText("0");
                        f6032q = Boolean.valueOf(false);
                    }
                    f6033r = Boolean.valueOf(false);
                    if (ah.booleanValue()) {
                        et_main.setText("");
                        f6025W = "";
                        f6024V = "";
                        f6026X = "";
                        ab = "";
                        tv_Display.setText("0");
                        ah = Boolean.valueOf(false);
                    }
                    if (aj.booleanValue() || ak.booleanValue() || am.booleanValue() || al.booleanValue() || an.booleanValue() || ao.booleanValue()) {
                        if (!f6030o.booleanValue()) {
                            ab = "";
                            f6030o = Boolean.valueOf(false);
                        }
                        aj = Boolean.valueOf(false);
                        ak = Boolean.valueOf(false);
                        am = Boolean.valueOf(false);
                        al = Boolean.valueOf(false);
                        an = Boolean.valueOf(false);
                        ao = Boolean.valueOf(false);
                    }
                    if (av.booleanValue()) {
                        ac += "0";
                    }
                    if (tv_Display.getText().toString().length() != 16 || f6029n.booleanValue()) {
                        if (tv_Display.getText().length() != 1 || !tv_Display.getText().toString().equalsIgnoreCase("0") || !et_main.getText().toString().equalsIgnoreCase("0")) {
                            et_main.append("0");
                            ab += "0";
                            tv_Display.setText(ab);
                            f6029n = Boolean.valueOf(false);
                        } else {
                            return;
                        }
                    }
                    if (f6027Y.booleanValue()) {
                        f6025W = ab;
                        Log.e("str2", f6025W);
                        return;
                    }
                    f6024V = ab;
                    Log.e("str2", f6024V);
                    return;
                }
                return;
            case R.id.ll_delete:
                f6033r = Boolean.valueOf(false);
                int length = et_main.getText().length();
                if (et_main.getText().toString().equals("Can't divide by 0")) {
                    f6029n = Boolean.valueOf(false);
                    et_main.setText("");
                    tv_Display.setText("0");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    f6033r = Boolean.valueOf(false);
                    f6031p = Boolean.valueOf(false);
                    f6032q = Boolean.valueOf(false);
                    ac = "";
                    prev = "";
                    firststr = "";
                    f6026X = "";
                    aC = false;
                    return;
                } else if (et_main.getText().toString().equals("Invalid Input")) {
                    f6029n = Boolean.valueOf(false);
                    et_main.setText("");
                    tv_Display.setText("0");
                    ab = "";
                    f6025W = "";
                    f6024V = "";
                    f6026X = "";
                    f6033r = Boolean.valueOf(false);
                    f6031p = Boolean.valueOf(false);
                    f6032q = Boolean.valueOf(false);
                    ac = "";
                    prev = "";
                    firststr = "";
                    f6026X = "";
                    aC = false;
                    return;
                } else {
                    if (length > 0) {
                        if (Character.isDigit(et_main.getText().charAt(length - 1)) || et_main.getText().toString().substring(length - 1).equalsIgnoreCase(".") || !Character.isDigit(et_main.getText().charAt(length - 2))) {
                            if (et_main.getText().charAt(length - 1) == '+' || et_main.getText().charAt(length - 1) == '-' || et_main.getText().charAt(length - 1) == '*' || et_main.getText().charAt(length - 1) == '/') {
                                Log.e("TAG", "here for back operator called : " + et_main.getText().charAt(length - 1));
                                firststr = "";
                            } else {
                                firststr = "";
                            }
                            et_main.getText().delete(length - 1, length);
                            if (!(et_main.getText().toString().equalsIgnoreCase("") || et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '-' || Character.isDigit(et_main.getText().length() - 1))) {
                                et_main.setText(et_main.getText().toString().substring(0, et_main.getText().toString().length() - 1));
                            }
                            if (ah.booleanValue()) {
                                ab = et_main.getText().toString();
                            } else if (f6025W != null) {

                                if (f6025W.equalsIgnoreCase("")) {
                                    ab = et_main.getText().toString();
                                } else {
                                    f6025W = f6025W.substring(0, f6025W.length() - 1);
                                    if (f6025W.equalsIgnoreCase("-")) {
                                        f6025W = "";
                                    }
                                    ab = f6025W;
                                }
                            }
                            tv_Display.setText(ab);
                            if (et_main.getText().toString().length() == 1 && !Character.isDigit(et_main.getText().toString().charAt(0))) {

                                tv_Display.setText("0");
                                et_main.setText("");
                                ab = "";
                            }
                        } else {

                            f6030o = Boolean.valueOf(true);
                            f6027Y = Boolean.valueOf(false);
                            et_main.getText().delete(length - 1, length);
                            ab = f6024V;
                            tv_Display.setText(ab);
                            Log.e("idis2", ab);
                            f6025W = "";
                        }
                    }
                    if (et_main.getText().length() == 0) {

                        et_main.setText("");
                        tv_Display.setText("0");
                        ab = "";
                    }
                    if (et_main.getText().length() == 0) {

                        f6029n = Boolean.valueOf(false);
                        et_main.setText("");
                        tv_Display.setText("0");
                        ab = "";
                        f6025W = "";
                        f6024V = "";
                        f6026X = "";
                        f6033r = Boolean.valueOf(false);
                        f6031p = Boolean.valueOf(false);
                        f6032q = Boolean.valueOf(false);
                        ac = "";
                        prev = "";
                        firststr = "";
                        f6026X = "";
                        aC = false;
                    }
                    aC = false;
                    return;
                }
            default:
                return;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        aB = false;
    }

    protected void onResume() {
        super.onResume();
        aB = true;

        Log.e("tag", "onResume");
        startBackgroundThread();
        if (textureView.isAvailable()) {
            openCamera(textureView.getWidth(), textureView.getHeight());
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }

    }

    protected void onStop() {
        super.onStop();
        aB = false;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                view.setAlpha(0.2f);
                view.callOnClick();
                break;
            case 1:
                view.setAlpha(1.0f);
                break;
        }
        return true;
    }

    public void sqrlEquals() {

        f6031p = Boolean.valueOf(true);
        String obj = et_main.getText().toString();
        if (!Character.isDigit(obj.charAt(obj.length() - 1))) {
            obj = obj.substring(0, obj.length() - 1);
        }
        Log.e("first", "" + obj.toString().charAt(0));

        if (Character.isDigit(obj.toString().charAt(0))) {

            Double valueOf = Double.valueOf(Double.parseDouble(new DecimalFormat(".########################################################").format((Double) new ExtendedDoubleEvaluator().evaluate(String.valueOf(obj)))));
            Log.e("add", "" + valueOf);
            if (tv_Display.length() != 0) {
                expressions = "sqrt(" + valueOf + ")";
            }
            if (expressions.length() == 0) {
                //expressions = IdManager.DEFAULT_VERSION_NAME;
            }
            DoubleEvaluator doubleEvaluator = new DoubleEvaluator();
            try {
                result = (Double) new ExtendedDoubleEvaluator().evaluate(expressions);
                NumberFormat instance = NumberFormat.getInstance();
                instance.setMaximumIntegerDigits(50);
                instance.setMaximumFractionDigits(50);
                instance.setGroupingUsed(false);
                obj = instance.format(result);
                if (obj.equalsIgnoreCase("nan")) {
                    tv_Display.setText("0");
                    et_main.setText("Invalid Input");
                    f6033r = Boolean.valueOf(true);
                } else {
                    if (obj.length() > 16) {
                        obj = obj.substring(0, 16);
                    } else {
                        tv_Display.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
                    }
                    tv_Display.setText(obj + "");
                    et_main.setText("" + obj);
                    ab = obj + "";
                }
                f6024V = tv_Display.getText().toString();
                f6025W = "";
                return;
            } catch (Exception e) {

                Log.e("TAG", "Toast invalid expression");
                Toast.makeText(this, "Invalid Expression", 0).show();
                et_main.setText("");
                tv_Display.setText("0");
                ab = "";
                ac = "";
                prev = "";
                firststr = "";
                aC = false;
                expressions = "";
                e.printStackTrace();
                return;
            }
        }
        tv_Display.setText("0");
        et_main.setText("Invalid Input");
        f6033r = Boolean.valueOf(true);
    }

    private void showSetPasswordDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_set_password_hint);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setLayout(-1, -2);
        }
        TextView btnCancel = (TextView) dialog.findViewById(R.id.btn_cancel);
        TextView btnOk = (TextView) dialog.findViewById(R.id.btn_ok);
        ((ImageView) dialog.findViewById(R.id.img_close)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
