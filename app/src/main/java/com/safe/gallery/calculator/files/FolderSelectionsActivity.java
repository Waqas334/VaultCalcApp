package com.safe.gallery.calculator.files;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.app.BaseActivity;
import com.safe.gallery.calculator.common.MergeAdapter;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class FolderSelectionsActivity extends BaseActivity {

    private static final Pattern DIR_SEPORATOR = Pattern.compile("/");
    Button New;
    Button all;
    Button cancel;
    private ArrayList<File> directoryList = new ArrayList();
    private ArrayList<String> directoryNames = new ArrayList();
    private ListView directoryView;
    private ArrayList<File> fileArrayList = new ArrayList();
    private ArrayList<String> fileNames = new ArrayList();
    int index = 0;
    File mainPath = new File(Environment.getExternalStorageDirectory() + "");
    Button ok;
    TextView path;
    String primary_sd;
    String secondary_sd;
    Button storage;
    Boolean switcher = Boolean.valueOf(false);
    int top = 0;

    class C05721 implements OnItemClickListener {
        C05721() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            int i = 0;
            index = directoryView.getFirstVisiblePosition();
            View v = directoryView.getChildAt(0);
            FolderSelectionsActivity folderSelectionsActivity = FolderSelectionsActivity.this;
            if (v != null) {
                i = v.getTop();
            }
            folderSelectionsActivity.top = i;
            try {
                if (position < directoryList.size()) {
                    mainPath = (File) directoryList.get(position);
                    loadLists();
                }
            } catch (Throwable th) {
                mainPath = mainPath;
                loadLists();
            }
        }
    }

    class C05732 implements OnClickListener {
        C05732() {
        }

        public void onClick(View v) {
            ok();
        }
    }

    class C05743 implements OnClickListener {
        C05743() {
        }

        public void onClick(View v) {
            finish();
        }
    }

    class C05754 implements OnClickListener {
        C05754() {
        }

        public void onClick(View v) {
            try {
                if (switcher.booleanValue()) {
                    mainPath = new File(primary_sd);
                    loadLists();
                    switcher = Boolean.valueOf(false);
                    storage.setText(getString(R.string.ext));
                    return;
                }
                mainPath = new File(secondary_sd);
                loadLists();
                switcher = Boolean.valueOf(true);
                storage.setText(getString(R.string.Int));
            } catch (Throwable th) {
            }
        }
    }

    class C05785 implements OnClickListener {

        class C05772 implements DialogInterface.OnClickListener {
            C05772() {
            }

            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }

        C05785() {
        }

        public void onClick(View v) {
            Builder alert = new Builder(v.getContext());
            alert.setTitle(getString(R.string.New));
            alert.setMessage(getString(R.string.CNew));
            final EditText input = new EditText(v.getContext());
            alert.setView(input);
            alert.setPositiveButton(getString(R.string.create), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String fileName = input.getText().toString();
                    if (fileName != null && fileName.length() > 0) {
                        new File(mainPath.getPath() + "/" + fileName + "/").mkdirs();
                        loadLists();
                    }
                }
            });
            alert.setNegativeButton(getString(R.string.cancel), new C05772());
            alert.show();
        }
    }

    class C05796 implements FileFilter {
        C05796() {
        }

        public boolean accept(File file) {
            return file.isFile();
        }
    }

    class C05807 implements FileFilter {
        C05807() {
        }

        public boolean accept(File file) {
            return file.isDirectory();
        }
    }

    class C05818 implements Comparator<File> {
        C05818() {
        }

        public int compare(File object1, File object2) {
            return object1.getName().compareTo(object2.getName());
        }
    }

    class C05829 implements Comparator<File> {
        C05829() {
        }

        public int compare(File object1, File object2) {
            return object1.getName().compareTo(object2.getName());
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_file_selection);

        findViews();
        initViews();


    }

    private void initViews() {

        all.setEnabled(false);
        loadLists();
        ExtStorageSearch();
        if (secondary_sd == null) {
            storage.setEnabled(false);
        }
        directoryView.setOnItemClickListener(new C05721());
        ok.setOnClickListener(new C05732());
        cancel.setOnClickListener(new C05743());
        storage.setOnClickListener(new C05754());
        New.setOnClickListener(new C05785());
    }

    private void findViews() {

        directoryView = (ListView) findViewById(R.id.directorySelectionList);
        ok = (Button) findViewById(R.id.ok);
        all = (Button) findViewById(R.id.all);
        cancel = (Button) findViewById(R.id.cancel);
        storage = (Button) findViewById(R.id.storage);
        New = (Button) findViewById(R.id.New);
        path = (TextView) findViewById(R.id.folderpath);
    }

    public void onBackPressed() {
        try {
            if (mainPath.equals(Environment.getExternalStorageDirectory().getParentFile().getParentFile())) {
                finish();
                return;
            }
            mainPath = mainPath.getParentFile();
            loadLists();
            directoryView.setSelectionFromTop(index, top);
        } catch (Throwable th) {
        }
    }

    public void ok() {
        Intent result = getIntent();
        result.putExtra("upload", mainPath);
        setResult(-1, result);
        finish();
    }

    private void loadLists() {

        int i = 0;
        FileFilter fileFilter = new C05796();
        File[] tempDirectoryList = mainPath.listFiles(new C05807());
        if (tempDirectoryList != null && tempDirectoryList.length > 1) {
            Arrays.sort(tempDirectoryList, new C05818());
        }
        directoryList = new ArrayList();
        directoryNames = new ArrayList();
        for (File file2 : tempDirectoryList) {
            directoryList.add(file2);
            directoryNames.add(file2.getName());
        }
        directoryView.setAdapter(new ArrayAdapter(this, 17367043, directoryNames));
        File[] tempFileList = mainPath.listFiles(fileFilter);
        if (tempFileList != null && tempFileList.length > 1) {
            Arrays.sort(tempFileList, new C05829());
        }
        fileArrayList = new ArrayList();
        fileNames = new ArrayList();
        int length = tempFileList.length;
        while (i < length) {
            File file2 = tempFileList[i];
            fileArrayList.add(file2);
            fileNames.add(file2.getName());
            i++;
        }
        path.setText(mainPath.toString());
        iconload();
    }

    public void iconload() {
        String[] filenames = (String[]) fileNames.toArray(new String[fileNames.size()]);
        CustomListSingleOnly adapter1 = new CustomListSingleOnly(this, (String[]) directoryNames.toArray((String[]) directoryNames.toArray(new String[directoryNames.size()])), mainPath.getPath());
        CustomListSingleOnly adapter2 = new CustomListSingleOnly(this, (String[]) fileNames.toArray(filenames), mainPath.getPath());
        MergeAdapter adap = new MergeAdapter();
        adap.addAdapter(adapter1);
        adap.addAdapter(adapter2);
        directoryView.setAdapter(adap);
    }

    public void ExtStorageSearch() {
        primary_sd = System.getenv("EXTERNAL_STORAGE");
        secondary_sd = System.getenv("SECONDARY_STORAGE");
        if (primary_sd == null) {
            primary_sd = Environment.getExternalStorageDirectory() + "";
        }
        if (secondary_sd == null) {
            for (String string : getStorageDirectories(this)) {
                if (new File(string).exists() && new File(string).isDirectory() && !string.equals(primary_sd)) {
                    secondary_sd = string;
                    return;
                }
            }
        }
    }

    public static String[] getStorageDirectories(Context context) {
        Set<String> rv = new HashSet();
        String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
        String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
        String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");
        if (!TextUtils.isEmpty(rawEmulatedStorageTarget)) {
            String rawUserId;
            if (VERSION.SDK_INT < 17) {
                rawUserId = "";
            } else {
                String[] folders = DIR_SEPORATOR.split(Environment.getExternalStorageDirectory().getAbsolutePath());
                String lastFolder = folders[folders.length - 1];
                boolean isDigit = false;
                try {
                    Integer.valueOf(lastFolder);
                    isDigit = true;
                } catch (NumberFormatException e) {
                }
                rawUserId = isDigit ? lastFolder : "";
            }
            if (TextUtils.isEmpty(rawUserId)) {
                rv.add(rawEmulatedStorageTarget);
            } else {
                rv.add(rawEmulatedStorageTarget + File.separator + rawUserId);
            }
        } else if (VERSION.SDK_INT >= 23) {
            for (File file : context.getExternalFilesDirs(null)) {
                String applicationSpecificAbsolutePath = file.getAbsolutePath();
                rv.add(applicationSpecificAbsolutePath.substring(0, applicationSpecificAbsolutePath.indexOf("Android/data")));
            }
        } else if (TextUtils.isEmpty(rawExternalStorage)) {
            rv.addAll(Arrays.asList(getPhysicalPaths()));
        } else {
            rv.add(rawExternalStorage);
        }
        if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
            Collections.addAll(rv, rawSecondaryStoragesStr.split(File.pathSeparator));
        }
        return (String[]) rv.toArray(new String[rv.size()]);
    }

    private static String[] getPhysicalPaths() {
        return new String[]{"/storage/sdcard0", "/storage/sdcard1", "/storage/extsdcard", "/storage/sdcard0/external_sdcard", "/mnt/extsdcard", "/mnt/sdcard/external_sd", "/mnt/external_sd", "/mnt/media_rw/sdcard1", "/removable/microsd", "/mnt/emmc", "/storage/external_SD", "/storage/ext_sd", "/storage/removable/sdcard1", "/data/sdext", "/data/sdext2", "/data/sdext3", "/data/sdext4", "/sdcard1", "/sdcard2", "/storage/microsd"};
    }
}
