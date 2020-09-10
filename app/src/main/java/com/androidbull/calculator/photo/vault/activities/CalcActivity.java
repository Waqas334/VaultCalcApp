package com.androidbull.calculator.photo.vault.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.androidbull.calculator.photo.R;
import com.androidbull.calculator.photo.vault.MainApplication;
import com.androidbull.calculator.photo.vault.MyBassActivity;
import com.androidbull.calculator.photo.vault.utils.AppConstants;
import com.androidbull.calculator.photo.vault.utils.AutoFitTextureView;
import com.androidbull.calculator.photo.vault.utils.ExtendedDoubleEvaluator;
import com.androidbull.calculator.photo.vault.utils.Utils;
import com.androidbull.calculator.photo.vault.utils.share.Share;
import com.androidbull.calculator.photo.vault.utils.share.share_calc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class CalcActivity extends MyBassActivity implements View.OnClickListener, View.OnTouchListener {

    private static final String TAG = "CalcActivity";
    private static SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 270);
        ORIENTATIONS.append(Surface.ROTATION_90, 180);
        ORIENTATIONS.append(Surface.ROTATION_180, 90);
        ORIENTATIONS.append(Surface.ROTATION_270, 0);
    }

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
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    private static final int MAX_PREVIEW_WIDTH = 1920;
    private static final int MAX_PREVIEW_HEIGHT = 1080;

    boolean mSurfaceTextureAvailable = false;
    boolean mPermissionsGranted = false;


    TextView tv_eight;

    TextView tv_five;

    TextView tv_two;

    TextView tv_dot;

    TextView tv_divide;

    TextView tv_nine;

    TextView tv_six;

    TextView tv_three;

    TextView tv_sign;

    TextView tv_mul;

    TextView tv_min;

    TextView tv_plus;

    TextView tv_equal;

    TextView tv_sqrt;

    //This is a round button which is hidden by default so I am gonna delete it
//    ImageView f6017O;

    EditText et_main;

    EditText tv_Display;

//    TextView tv_divide;

    //Delete button in EditText that is invisible by default
//    LinearLayout ll_delete;

    //THis was the Linear Layout for button,
//    LinearLayout ll_calc;

    //THis was the Relative Layout for button,
//    RelativeLayout rl_calc_layout;

    String f6024V = "";

    String f6025W = "";

    String f6026X = "";

    Boolean f6027Y = false;

    Editable f6028Z;

    boolean aB;
    boolean aC = false;
    Double aa;
    String ab = "";
    String ac = "";
    int ad = 0;
    //    int ae = 0;
//    int af = 0;
//    int ag = 0;
    Boolean ah = false;
    Boolean ai = false;
    Boolean aj = false;
    Boolean ak = false;
    Boolean al = false;
    Boolean am = false;
    Boolean an = false;
    Boolean ao = false;
    Boolean ap = false;
    Boolean aq = false;
    Boolean ar = true;
    //    Boolean as = true;
//    Boolean at = true;
//    Boolean au = true;
    Boolean av = false;
    //    Boolean aw = false;
    Boolean az = false;
    private String expressions = "";
    private String firststr = "";

    Boolean f6029n = false;

    Boolean f6030o = false;

    Boolean f6031p = false;

    Boolean f6032q = false;

    Boolean f6033r = false;

    TextView tv_clear;

    TextView tv_seven;

    TextView tv_four;

    TextView tv_one;

    TextView tv_zero;

    TextView tv_percent;

    //my variable
    String firstString = "";
    String secondString = "";
    String oprator = "";
    String completeString = "";
    boolean opererationSelected = false;
    boolean firstOpratorSelected = false;
    double answer;


    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_calc);

        //TODO CLEANING: Checkout why it is being used even
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
            Log.e("TAG", "KeyHash:--->" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }

        textureView = findViewById(R.id.textureView);
        textureView.setSurfaceTextureListener(textureListener);
        initviews();
        initlisteners();
        check_tablet();

        if (MainApplication.getInstance().getPassword().isEmpty()) {

            //It means that password is not set
            //Show password set dialog
            showSetPasswordDialog();

        }


    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, final int width, final int height) {
            mSurfaceTextureAvailable = true;


            if (ContextCompat.checkSelfPermission(CalcActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                //Camera Permission not granted
                ActivityCompat.requestPermissions(CalcActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            mPermissionsGranted = true;

            // Execute some code after 10 milliseconds have passed
            Handler handler = new Handler();
            handler.postDelayed(() -> {

                if (mSurfaceTextureAvailable && mPermissionsGranted) {
                    openCamera(width, height);
                }
                //setupCameraIfPossible();
            }, 10);


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

        @Override
        public void onClosed(@NonNull CameraDevice camera) {
            super.onClosed(camera);
//            finish();
        }
    };

    private void openCamera(int width, int height) {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.e("tag", "is camera open");

        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            String cameraId = manager.getCameraIdList()[1];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;

            Size largest = Collections.max(
                    Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                    new Utils.CompareSizesByArea());


            int displayRotation = getWindowManager().getDefaultDisplay().getRotation();
            //noinspection ConstantConditions
            int mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
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

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CalcActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (Exception e) {
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
                    Toast.makeText(CalcActivity.this, getString(R.string.config_change), Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (Exception e) {
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
        } catch (Exception e) {
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
                Toast.makeText(CalcActivity.this, getResources().getString(R.string.permission_not_granted), Toast.LENGTH_LONG).show();
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
        Log.i(TAG, "takePicture: executed");
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, mBackgroundHandler);
        } catch (Exception e) {
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

        private void save(byte[] bytes) throws IOException {
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P){


            }
            OutputStream output = null;
            try {

                Date todayDate = Calendar.getInstance().getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String todayString = formatter.format(todayDate);
                Log.e("TAG", "save:format -->" + todayString);

                File f = new File(AppConstants.INTRUDER_PATH);
                File file = new File(AppConstants.INTRUDER_PATH + "/" + todayString + ".jpg");

                Log.i(TAG, "save: Intruder image is saved at: " + f.getAbsolutePath() + "\nFile: " + file.getAbsolutePath());

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
            if (calcActivity.tv_Display.getText().toString().equalsIgnoreCase("")
                    || calcActivity.tv_Display.getText().toString().equalsIgnoreCase("0")) {
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
//        f6017O = (ImageView) findViewById(R.id.iv_square_root);
//        f6017O.setOnClickListener(this);
        if (share_calc.flag_expand) {
            Log.e("flag_expand", "" + share_calc.flag_expand);
            share_calc.flag_expand = false;
        }
    }

    private void dot_operation() {
        if (et_main.getText().length() > 0) {
            ad = 0;
            if (ai) {
                ah = false;
            }
            if (ah) {
                et_main.setText("");
                ab = "";
                tv_Display.setText("0");
                ah = false;
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
            if (ad == 0 && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1)
                    != '.' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1)
                    != '+' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1)
                    != '-' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1)
                    != '/' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1)
                    != '*' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1)
                    != '%') {
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
                if (av) {
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

        tv_zero.setOnClickListener(this);
        tv_one.setOnClickListener(this);
        tv_two.setOnClickListener(this);
        tv_three.setOnClickListener(this);
        tv_four.setOnClickListener(this);
        tv_five.setOnClickListener(this);
        tv_six.setOnClickListener(this);
        tv_seven.setOnClickListener(this);
        tv_eight.setOnClickListener(this);
        tv_nine.setOnClickListener(this);
        tv_plus.setOnClickListener(this);
        tv_min.setOnClickListener(this);
        tv_mul.setOnClickListener(this);
        tv_divide.setOnClickListener(this);
        tv_percent.setOnClickListener(this);
        tv_clear.setOnClickListener(this);
        tv_equal.setOnClickListener(this);
        tv_sqrt.setOnClickListener(this);
//        ll_delete.setOnClickListener(this);
        tv_sign.setOnClickListener(this);
        tv_dot.setOnClickListener(this);
        tv_zero.setOnTouchListener(this);
        tv_one.setOnTouchListener(this);
        tv_two.setOnTouchListener(this);
        tv_three.setOnTouchListener(this);
        tv_four.setOnTouchListener(this);
        tv_five.setOnTouchListener(this);
        tv_six.setOnTouchListener(this);
        tv_seven.setOnTouchListener(this);
        tv_eight.setOnTouchListener(this);
        tv_nine.setOnTouchListener(this);
        tv_plus.setOnTouchListener(this);
        tv_min.setOnTouchListener(this);
        tv_mul.setOnTouchListener(this);
        tv_divide.setOnTouchListener(this);
        tv_percent.setOnTouchListener(this);
        tv_clear.setOnTouchListener(this);
        tv_equal.setOnTouchListener(this);
        tv_sqrt.setOnTouchListener(this);
//        ll_delete.setOnTouchListener(this);
        tv_sign.setOnTouchListener(this);
        tv_dot.setOnTouchListener(this);
        tv_Display.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        tv_Display.setSingleLine();
        tv_Display.addTextChangedListener(new C16052(this));
        et_main.setSingleLine();
        tv_Display.setSingleLine();
        et_main.addTextChangedListener(new C16063(this));

    }

    private void initviews() {

        tv_clear = findViewById(R.id.tv_clear);
        tv_seven = findViewById(R.id.tv_seven);
        tv_four = findViewById(R.id.tv_four);
        tv_one = findViewById(R.id.tv_one);
        tv_zero = findViewById(R.id.tv_zero);
        tv_percent = findViewById(R.id.tv_percent);
        tv_eight = findViewById(R.id.tv_eight);
        tv_five = findViewById(R.id.tv_five);
        tv_two = findViewById(R.id.tv_two);
        tv_dot = findViewById(R.id.tv_dot);
        tv_divide = findViewById(R.id.tv_divide);
        tv_nine = findViewById(R.id.tv_nine);
        tv_six = findViewById(R.id.tv_six);
        tv_three = findViewById(R.id.tv_three);
        tv_sign = findViewById(R.id.tv_sqrt);
        tv_mul = findViewById(R.id.tv_mul);
        tv_min = findViewById(R.id.tv_min);
        tv_plus = findViewById(R.id.tv_plus);
        tv_equal = findViewById(R.id.tv_equal);
        tv_sqrt = findViewById(R.id.tv_sqrt);
        et_main = findViewById(R.id.et_main);
        tv_Display = findViewById(R.id.tv_Display);
//        tv_divide = (TextView) findViewById(R.id.tv_divide);
//        ll_delete = (LinearLayout) findViewById(R.id.ll_delete);
//        ll_calc = (LinearLayout) findViewById(R.id.ll_calc);
//        rl_calc_layout = (RelativeLayout) findViewById(R.id.rl_calc_layout);

    }


    //TODO : OnClick
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_clear:
                et_main.setText("");
                tv_Display.setText("0");
                opererationSelected = false;
                firstString = "";
                secondString = "";
                oprator = "";
                return;
            case R.id.tv_dot:
                tv_Display.setText("");
                dotOpration();

                return;

            case R.id.tv_nine:
                tv_Display.setText("");
                getvalue("9");
                return;

            case R.id.tv_eight:
                tv_Display.setText("");
                getvalue("8");
                return;

            case R.id.tv_seven:
                tv_Display.setText("");
                getvalue("7");
                return;

            case R.id.tv_six:
                tv_Display.setText("");
                getvalue("6");
                return;

            case R.id.tv_five:
                tv_Display.setText("");
                getvalue("5");
                return;

            case R.id.tv_four:
                tv_Display.setText("");
                getvalue("4");
                return;

            case R.id.tv_three:
                tv_Display.setText("");
                getvalue("3");
                return;
            case R.id.tv_two:
                tv_Display.setText("");
                getvalue("2");
                return;

            case R.id.tv_one:
                tv_Display.setText("");
                getvalue("1");
                return;
            case R.id.tv_zero:
                tv_Display.setText("");
                getvalue("0");
                return;


            //oprators

            case R.id.tv_plus:
                if (!(tv_Display.getText().toString().isEmpty())){
                    opererationSelected = true;
                    oprator = "+";
                    firstString = tv_Display.getText().toString();
                    completeString = firstString + " " + oprator +" ";
                    et_main.setText(completeString);
                    tv_Display.setText("");

                }
                else if (firstString.isEmpty()) {
                    Toast.makeText(this, "First Enter a number", Toast.LENGTH_SHORT).show();
                } else {
                    opererationSelected = true;
                    oprator = "+";
                    completeString = firstString + " " + oprator +" ";
                    et_main.setText(completeString);
                }
                return;

            case R.id.tv_min:
                if (!(tv_Display.getText().toString().isEmpty())){
                    opererationSelected = true;
                    oprator = "-";
                    firstString = tv_Display.getText().toString();
                    completeString = firstString + " " + oprator +" ";
                    et_main.setText(completeString);
                    tv_Display.setText("");

                }
                else if (firstString.isEmpty()) {
                    Toast.makeText(this, "First Enter a number", Toast.LENGTH_SHORT).show();

                } else {
                    opererationSelected = true;
                    oprator = "-";
                    completeString = firstString + " " + oprator +" ";
                    et_main.setText(completeString);
                }

                return;

            case R.id.tv_mul:
                if (!(tv_Display.getText().toString().isEmpty())){
                    opererationSelected = true;
                    oprator = "*";
                    firstString = tv_Display.getText().toString();
                    completeString = firstString + " " + oprator +" ";
                    et_main.setText(completeString);
                    tv_Display.setText("");

                }
                else if (firstString.isEmpty()) {
                    Toast.makeText(this, "First Enter a number", Toast.LENGTH_SHORT).show();
                }
                else {
                    opererationSelected = true;
                    oprator = "*";
                    completeString = firstString + " " + oprator +" ";
                    et_main.setText(completeString);
                }

                return;

            case R.id.tv_divide:
                if (!(tv_Display.getText().toString().isEmpty())){
                    opererationSelected = true;
                    oprator = "/";
                    firstString = tv_Display.getText().toString();
                    completeString = firstString + " " + oprator +" ";
                    et_main.setText(completeString);
                    tv_Display.setText("");

                }
                else if (firstString.isEmpty()) {
                    Toast.makeText(this, "First Enter a number", Toast.LENGTH_SHORT).show();
                }
                else {
                    opererationSelected = true;
                    oprator = "/";
                    completeString = firstString + " " + oprator +" ";
                    et_main.setText(completeString);
                }

                return;

            case R.id.tv_sqrt:
                if (et_main.getText().toString().isEmpty()) {
                    Toast.makeText(this, "First Enter Number", Toast.LENGTH_SHORT).show();

                }
                else {
                    double val = Double.valueOf(et_main.getText().toString());
                    answer = val * val;
                    tv_Display.setText(Double.toString(answer));
                    et_main.setText("");
                    firstString = "";
                }
                return;

            case R.id.tv_equal:

                if (opererationSelected) {

                    if (!(secondString.isEmpty())) {
                        answer = calculateResult(oprator);
                        tv_Display.setText(Double.toString(answer));
                        et_main.setText("");
                        firstString = "";
                        secondString = "";
                        opererationSelected = false;
                        oprator = "";
                        completeString = "";
                    }
                } else {


                    if (!MainApplication.getInstance().getPassword().equals("")) {

                        Log.i(TAG, "onClick: Password is already set");
                        //Means that someone have set it's password
                        String cpass = et_main.getText().toString();
                        if (MainApplication.getInstance().getPassword().equalsIgnoreCase(cpass)) {
                            //Correct password
                            //Now check if user have set the security question or not
                            Log.i(TAG, "onClick: Correct Password Entered");

                            String secretQuestion = MainApplication.getInstance().getSecurityQuestion();
                            if (TextUtils.isEmpty(secretQuestion)) {
                                //Security question is not set.
                                //Start add security question activity
                                Log.i(TAG, "onClick: Security question was not added, so adding one");
                                startActivity(new Intent(CalcActivity.this, SecurityQuestionActivity.class).putExtra(SecurityQuestionActivity.TYPE, SecurityQuestionActivity.ADD));
                                finish();
                                return;
                            }
                            //Password is correct and security question is already added so now start home activity
                            startActivity(new Intent(CalcActivity.this, HomeActivity.class));
                            finish();
                            return;
                        } else if (cpass.equals("11223344")) {
                            Log.i(TAG, "onClick: Master password is entered");
                            //Asking to reset password
                            //Start the security question activity
                            startActivity(new Intent(this, SecurityQuestionActivity.class).putExtra(SecurityQuestionActivity.TYPE, SecurityQuestionActivity.FORGOT_PASS));

                        } else if (!f6033r) {
                            Log.i(TAG, "onClick: Boolean value is false");

                            takePicture();
//                            if (MainApplication.getInstance().getPassword().length() == cpass.length()) {
//                                Log.i(TAG, "onClick: Both have same length");
//
//
//                            }

                            if (!(!ah
                                    || f6026X.equals("")
                                    || f6025W == null
                                    || f6025W.equalsIgnoreCase(""))) {
                                Log.e("str2equal", f6025W);
                                f6028Z = new SpannableStringBuilder(tv_Display.getText().toString() + f6026X + f6025W);

                            }
                            if (et_main.getText().length() <= 0) {

                                Toast.makeText(this, getResources().getString(R.string.select_num_first), Toast.LENGTH_SHORT).show();
                                return;

                            } else if (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1)
                                    != '+' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1)
                                    != '-' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1)
                                    != '/' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1)
                                    != '*' && et_main.getText().toString().charAt(et_main.getText().toString().length() - 1)
                                    != '%') {

                                aj = false;
                                ak = false;
                                am = false;
                                al = false;
                                an = false;
                                ao = false;
                                f6028Z = et_main.getText();
                                ah = true;

                                ac = "";
                                return;
                            } else {

                            }
                        }
                        return;
                    }
                    if (MainApplication.getInstance().getPassword().equals("")) {
                        //It's first time user opened the app
                        //So ask to set password

                        if (et_main.getText().length() != 4) {
                            //Password was less or more than 4 digits
                            Toast.makeText(this, getString(R.string.password_must_be_4_digit), Toast.LENGTH_SHORT).show();
                            return;
                        }


                        //Password was 4 digit long
                        //Starting the confirm password activity
                        Share.pass = Integer.parseInt(et_main.getText().toString());
                        Intent i = new Intent(CalcActivity.this, ConfirmCalcActivity.class);
                        startActivity(i);


                        return;
                    }
                }

                return;

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
            case MotionEvent.ACTION_DOWN:
                view.setAlpha(0.2f);
                view.callOnClick();
                break;
            case MotionEvent.ACTION_UP:
                view.setAlpha(1.0f);
                break;
        }
        return true;
    }

    public void sqrlEquals() {

        f6031p = true;
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
            try {
                Double result = new ExtendedDoubleEvaluator().evaluate(expressions);
                NumberFormat instance = NumberFormat.getInstance();
                instance.setMaximumIntegerDigits(50);
                instance.setMaximumFractionDigits(50);
                instance.setGroupingUsed(false);
                obj = instance.format(result);
                if (obj.equalsIgnoreCase("nan")) {
                    tv_Display.setText("0");
                    et_main.setText(getString(R.string.invalid_input));
                    f6033r = true;
                } else {
                    if (obj.length() > 16) {
                        obj = obj.substring(0, 16);
                    } else {
                        tv_Display.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
                    }
                    tv_Display.setText(obj);
                    et_main.setText(obj);
                    ab = obj + "";
                }
                f6024V = tv_Display.getText().toString();
                f6025W = "";
                return;
            } catch (Exception e) {

                Log.e("TAG", "Toast invalid expression");
                Toast.makeText(this, getString(R.string.invalid_expression), Toast.LENGTH_SHORT).show();
                et_main.setText("");
                tv_Display.setText(getString(R.string._0));
                ab = "";
                ac = "";
                firststr = "";
                aC = false;
                expressions = "";
                e.printStackTrace();
                return;
            }
        }
        tv_Display.setText(getString(R.string._0));
        et_main.setText(getString(R.string.invalid_input));
        f6033r = true;
    }

    private void showSetPasswordDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_templete);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setLayout(-1, -2);
        }
        TextView btnCancel = dialog.findViewById(R.id.btn_cancel);
        TextView btnOk = dialog.findViewById(R.id.btn_ok);
        dialog.findViewById(R.id.img_close).setOnClickListener(view -> dialog.dismiss());
        btnCancel.setOnClickListener(view -> dialog.dismiss());
        btnOk.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }


    //get value method
    public void getvalue(String value) {
        if (opererationSelected) {
            secondString += value;
            completeString = completeString + value;
            et_main.setText(completeString);
//            answer = calculateResult(oprator);
//            tv_Display.setText(Double.toString(answer));
//            firstString = Double.toString(answer);
//            secondString = "";

        } else {
            firstString += value;
            et_main.setText(firstString);
        }
    }

    //dot opration
    public void dotOpration() {
        if (opererationSelected) {
            if (!secondString.contains(".")) {
                secondString += ".";
                completeString += ".";
                et_main.setText(completeString);
            }

        } else {
            if (!firstString.contains(".")) {
                firstString += ".";

                et_main.setText(firstString);
            }
        }
    }

    //calculate result
    public double calculateResult(String oprator) {
        double ans = 0;

        switch (oprator) {
            case "-":
                ans = Double.valueOf(firstString) - Double.valueOf(secondString);
                firstString = "";
                secondString = "";
                oprator = "";
                opererationSelected = false;

                break;
            case "+":
                ans = Double.valueOf(firstString) + Double.valueOf(secondString);
                firstString = "";
                secondString = "";
                oprator = "";
                opererationSelected = false;
                break;

            case "/":
                if (secondString == "0") {
                    tv_Display.setText("infinity");
                    firstString = "";
                    secondString = "";
                    oprator = "";
                    opererationSelected = false;
                } else {
                    ans = Double.valueOf(firstString) / Double.valueOf(secondString);
                    firstString = "";
                    secondString = "";
                    oprator = "";
                    opererationSelected = false;
                }
                break;
            case "*":
                ans = Double.valueOf(firstString) * Double.valueOf(secondString);
                break;
        }
        return ans;

    }

    public void validateValues() {

    }


}
