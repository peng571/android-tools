//package org.pengyr.tool.core.tool;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.preference.PreferenceManager;
//import android.support.annotation.Nullable;
//
//import java.util.Map;
//import java.util.Set;
//
//public class SecurePreferences implements SharedPreferences {
//
//
//    private static SharedPreferences sFile;
//    private static byte[] sKey;
//
//    /**
//     * Constructor.
//     *
//     * @param context the caller's context
//     */
//    public SecurePreferences(Context context) {
//        // Proxy design pattern
//        if (SecurePreferences.sFile == null) {
//            SecurePreferences.sFile = PreferenceManager.getDefaultSharedPreferences(context);
//        }
//        // Initialize encryption/decryption key
//        try {
//            final String key = SecurePreferences.setAseKey(context);
//            String value = SecurePreferences.sFile.getString(key, null);
//            if (value == null) {
//                value = SecurePreferences.generateAesKeyValue();
//                SecurePreferences.sFile.edit().putString(key, value).commit();
//            }
//            SecurePreferences.sKey = SecurePreferences.decode(value);
//        } catch (Exception e) {
//            throw new IllegalStateException(e);
//        }
//    }
//
//    public void setAseKey(Context context, String key){
//
//    }
//
//    @Override
//    public String getString(String key, String defaultValue) {
//        final String encryptedValue =
//                SecurePreferences.sFile.getString(SecurePreferences.encrypt(key), null);
//        return (encryptedValue != null) ? SecurePreferences.decrypt(encryptedValue) : defaultValue;
//    }
//
//    @Nullable
//    @Override
//    public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
//        return null;
//    }
//
//    @Override
//    public int getInt(String key, int defValue) {
//        return 0;
//    }
//
//    @Override
//    public long getLong(String key, long defValue) {
//        return 0;
//    }
//
//    @Override
//    public float getFloat(String key, float defValue) {
//        return 0;
//    }
//
//    @Override
//    public boolean getBoolean(String key, boolean defValue) {
//        return false;
//    }
//
//    @Override
//    public boolean contains(String key) {
//        return false;
//    }
//
//    @Override
//    public Editor edit() {
//        return null;
//    }
//
//    @Override
//    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
//
//    }
//
//    @Override
//    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
//
//    }
//
//    @Override
//    public SharedPreferences.Editor putString(String key, String value) {
//        mEditor.putString(SecurePreferences.encrypt(key), SecurePreferences.encrypt(value));
//        return this;
//    }
//
//
//    @Override
//    public Map<String, ?> getAll() {
//        return null;
//    }
//
//    @Nullable
//    @Override
//    public String getString(String key, @Nullable String defValue) {
//        return null;
//    }
//}