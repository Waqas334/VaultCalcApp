package com.androidbull.calculator.photo.vault.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.ColorDrawable;
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
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.androidbull.calculator.photo.R;
import com.androidbull.calculator.photo.vault.MainApplication;
import com.androidbull.calculator.photo.vault.MyBassActivity;
import com.androidbull.calculator.photo.vault.utils.AppConstants;
import com.androidbull.calculator.photo.vault.utils.AutoFitTextureView;
import com.androidbull.calculator.photo.vault.utils.Utils;
import com.androidbull.calculator.photo.vault.utils.share.Share;
import com.androidbull.calculator.photo.vault.utils.share.share_calc;
import com.simplemobiletools.calculator.helpers.Calculator;
import com.simplemobiletools.calculator.helpers.CalculatorImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static com.simplemobiletools.calculator.helpers.ConstantsKt.DIGIT;
import static com.simplemobiletools.calculator.helpers.ConstantsKt.DIVIDE;
import static com.simplemobiletools.calculator.helpers.ConstantsKt.MINUS;
import static com.simplemobiletools.calculator.helpers.ConstantsKt.MULTIPLY;
import static com.simplemobiletools.calculator.helpers.ConstantsKt.PERCENT;
import static com.simplemobiletools.calculator.helpers.ConstantsKt.PLUS;
import static com.simplemobiletools.calculator.helpers.ConstantsKt.ROOT;

public class CalculatorActivity extends MyBassActivity implements Calculator {

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

    private static final int REQUEST_CAMERA_PERMISSION = 200;

    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    private AutoFitTextureView textureView;
    private final Semaphore mCameraOpenCloseLock = new Semaphore(1);

    private static final int MAX_PREVIEW_WIDTH = 1920;
    private static final int MAX_PREVIEW_HEIGHT = 1080;

    boolean mSurfaceTextureAvailable = false;
    boolean mPermissionsGranted = false;


    private TextView tvOne, tvTwo, tvThree, tvFour, tvFive, tvSix, tvSeven, tvEight, tvNine, tvZero, tvDecimal,
            tvClear, tvPercent, tvDivide, tvMultiply, tvMinus, tvPlus, tvEqual, tvSqrt, tvFormula, tvResult;
    private CalculatorImpl calculator;
    boolean operationSelected = false;
    private SharedPreferences sharedPreferences;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_calculator);

        textureView = findViewById(R.id.textureView);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        initViews();
        initListeners();
        checkTablet();

        if (MainApplication.getInstance().getPassword().isEmpty()) {
            showSetPasswordDialog();
        }

        calculator = new CalculatorImpl(this, this);

    }

    private void initViews() {

        tvOne = findViewById(R.id.tvOne);
        tvTwo = findViewById(R.id.tvTwo);
        tvThree = findViewById(R.id.tvThree);
        tvFour = findViewById(R.id.tvFour);
        tvFive = findViewById(R.id.tvFive);
        tvSix = findViewById(R.id.tvSix);
        tvSeven = findViewById(R.id.tvSeven);
        tvEight = findViewById(R.id.tvEight);
        tvNine = findViewById(R.id.tvNine);
        tvZero = findViewById(R.id.tvZero);
        tvDecimal = findViewById(R.id.tvDecimal);

        tvClear = findViewById(R.id.tvClear);
        tvPercent = findViewById(R.id.tvPercent);
        tvDivide = findViewById(R.id.tvDivide);
        tvMultiply = findViewById(R.id.tvMultiply);
        tvMinus = findViewById(R.id.tvMinus);
        tvPlus = findViewById(R.id.tvPlus);
        tvEqual = findViewById(R.id.tvEqual);
        tvSqrt = findViewById(R.id.tvSqrt);

        tvFormula = findViewById(R.id.tvFormula);
        tvResult = findViewById(R.id.tvResult);


    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListeners() {

        for (TextView textView : Arrays.asList(tvOne, tvTwo, tvThree, tvFour, tvFive, tvSix, tvSeven, tvEight, tvNine, tvZero, tvDecimal)) {
            textView.setOnClickListener(view -> calculator.numpadClicked(view.getId()));
        }

        tvPlus.setOnClickListener(view -> {
            calculator.handleOperation(PLUS);
            setFormula("+", this);
            operationSelected = true;

        });
        tvMinus.setOnClickListener(view -> {
            calculator.handleOperation(MINUS);
            setFormula("-", this);
            operationSelected = true;
        });
        tvMultiply.setOnClickListener(view -> {
            calculator.handleOperation(MULTIPLY);
            setFormula("*", this);
            operationSelected = true;
        });
        tvDivide.setOnClickListener(view -> {
            calculator.handleOperation(DIVIDE);
            setFormula("/", this);
            operationSelected = true;
        });
        tvPercent.setOnClickListener(view -> {
            calculator.handleOperation(PERCENT);
            setFormula("%", this);
            operationSelected = true;
        });
        tvSqrt.setOnClickListener(view -> {
            calculator.handleOperation(ROOT);
            setFormula("√", this);
            operationSelected = true;
        });


        tvClear.setOnClickListener(view -> {
            calculator.handleClear();
        });

        tvClear.setOnLongClickListener(view -> {
            calculator.handleReset();
            operationSelected = false;
            return true;
        });

        tvEqual.setOnClickListener(view -> handleEqual());


    }

    private void handleEqual() {
        if (operationSelected) {
            calculator.handleEquals();
        } else {

            if (!MainApplication.getInstance().getPassword().equals("")) {

                //Means that someone have set it's password
                String cpass = tvResult.getText().toString();
                if (MainApplication.getInstance().getPassword().equalsIgnoreCase(cpass)) {
                    //Correct password
                    //Now check if user have set the security question or not
                    Log.i(TAG, "onClick: Correct Password Entered");

                    String secretQuestion = MainApplication.getInstance().getSecurityQuestion();
                    if (TextUtils.isEmpty(secretQuestion)) {
                        //Security question is not set.
                        //Start add security question activity
                        Log.i(TAG, "onClick: Security question was not added, so adding one");
                        startActivity(new Intent(CalculatorActivity.this, SecurityQuestionActivity.class).putExtra(SecurityQuestionActivity.TYPE, SecurityQuestionActivity.ADD));
                        finish();
                        return;
                    }
                    //Password is correct and security question is already added so now start home activity
                    startActivity(new Intent(CalculatorActivity.this, HomeActivity.class));
                    finish();
                } else if (cpass.equals(getString(R.string.master_password))) {
                    Log.i(TAG, "onClick: Master password is entered");
                    //Asking to reset password
                    //Start the security question activity
                    startActivity(new Intent(this, SecurityQuestionActivity.class).putExtra(SecurityQuestionActivity.TYPE, SecurityQuestionActivity.FORGOT_PASS));
                    finish();
                } else {
                    Log.i(TAG, "onClick: Boolean value is false");
                    takePicture();
                }
            } else {
                if (tvResult.getText().length() != 4) {
                    Toast.makeText(this, getString(R.string.password_must_be_4_digit), Toast.LENGTH_SHORT).show();
                    return;
                }

                Share.pass = Integer.parseInt(tvResult.getText().toString());
                Intent i = new Intent(CalculatorActivity.this, ConfirmCalcActivity.class);
                startActivity(i);
            }
        }
    }

    @Override
    public void setValue(@NonNull String value, @NonNull Context context) {
        tvResult.setText(value);

    }

    @Override
    public void setValueBigDecimal(@NonNull BigDecimal d) {
        calculator.setValue(d.toString()/*Formatter.bigDecimalToString(d)*/);
        calculator.setLastKey(DIGIT);
    }

    @Override
    public void setFormula(@NonNull String value, @NonNull Context context) {
        tvFormula.setText(value);

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

    private void checkTablet() {
//        f6017O = (ImageView) findViewById(R.id.iv_square_root);
//        f6017O.setOnClickListener(this);
        if (share_calc.flag_expand) {
            Log.e("flag_expand", "" + share_calc.flag_expand);
            share_calc.flag_expand = false;
        }
    }

    @Override
    protected void onPause() {
        Log.e("tag", "onPause");

        closeCamera();
        stopBackgroundThread();

        super.onPause();
    }

    protected void onResume() {
        super.onResume();
        Log.e("tag", "onResume");
        checkIntruderMode();

    }

    private void checkIntruderMode() {
        boolean intruderMode = sharedPreferences.getBoolean(getString(R.string.pref_intruder_mode_key), false);
        if (intruderMode) {
            Log.e("tagtagtag", "intruderMode On");
            if (hasCameraPermissions()) {
                startIntruderMode();
            } else {
                sharedPreferences.edit().putBoolean(getString(R.string.pref_intruder_mode_key), false).apply();
                Log.e("tagtagtag", "intruderMode disabled");
                Toast.makeText(this, R.string.str_permission_required_intruder_mode_disabled, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.e("tagtagtag", "intruderMode Off");
        }
    }

    private void startIntruderMode() {
        Log.e("tagtagtag", "intruderMode Started");
        startBackgroundThread();
        if (textureView.isAvailable()) {
            openCamera(textureView.getWidth(), textureView.getHeight());
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    private boolean hasCameraPermissions() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestCameraPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
    }


    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }


    protected void stopBackgroundThread() {
        if (mBackgroundThread != null) {
            mBackgroundThread.quitSafely();
            try {
                mBackgroundThread.join();
                mBackgroundThread = null;
                mBackgroundHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, final int width, final int height) {
            mSurfaceTextureAvailable = true;


            if (ContextCompat.checkSelfPermission(CalculatorActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                //Camera Permission not granted
                ActivityCompat.requestPermissions(CalculatorActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
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
            textureView.setTransform(Utils.configureTransform(width, height, imageDimension, CalculatorActivity.this));
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


    protected void createCameraPreview() {
        Log.e("tag", "createCameraPreview");
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
                    Toast.makeText(CalculatorActivity.this, getString(R.string.config_change), Toast.LENGTH_SHORT).show();
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
                textureView.setTransform(Utils.configureTransform(width, height, imageDimension, CalculatorActivity.this));
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CalculatorActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("tag", "openCamera X");
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
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {


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

}
