# component
收集汇总通用的工具

## Manifest merger failed : Attribute provider  解决方案样例  在<provider>标签中添加  tools:replace="android:authorities"语句
 ``<provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="com.gs.component.FileProvider"
            android:exported="false"
            android:grantUriPermissions="true"
            tools:replace="android:authorities">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_path" />
        </provider>

