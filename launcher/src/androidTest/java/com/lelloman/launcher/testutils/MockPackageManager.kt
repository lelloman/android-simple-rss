package com.lelloman.launcher.testutils

import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.ChangedPackages
import android.content.pm.FeatureInfo
import android.content.pm.InstrumentationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageInstaller
import android.content.pm.PackageManager
import android.content.pm.PermissionGroupInfo
import android.content.pm.PermissionInfo
import android.content.pm.ProviderInfo
import android.content.pm.ResolveInfo
import android.content.pm.ServiceInfo
import android.content.pm.SharedLibraryInfo
import android.content.pm.VersionedPackage
import android.content.res.Resources
import android.content.res.XmlResourceParser
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.UserHandle

class MockPackageManager : PackageManager() {

    var queryIntentActivitiesResult = mutableListOf<ResolveInfo>()

    override fun canonicalToCurrentPackageNames(names: Array<out String>?): Array<String> = TODO()

    override fun getLaunchIntentForPackage(packageName: String): Intent? = TODO()

    override fun getResourcesForApplication(app: ApplicationInfo?): Resources = TODO()

    override fun getResourcesForApplication(appPackageName: String?): Resources = TODO()

    override fun getProviderInfo(component: ComponentName?, flags: Int): ProviderInfo = TODO()

    override fun getReceiverInfo(component: ComponentName?, flags: Int): ActivityInfo = TODO()

    override fun queryIntentActivityOptions(caller: ComponentName?, specifics: Array<Intent>?, intent: Intent?, flags: Int): MutableList<ResolveInfo> = TODO()

    override fun clearPackagePreferredActivities(packageName: String?) = TODO()

    override fun getPackageInstaller(): PackageInstaller = TODO()

    override fun resolveService(intent: Intent?, flags: Int): ResolveInfo = TODO()

    override fun verifyPendingInstall(id: Int, verificationCode: Int) = TODO()

    override fun getInstantAppCookie(): ByteArray = TODO()

    override fun getApplicationIcon(info: ApplicationInfo?): Drawable = TODO()

    override fun getApplicationIcon(packageName: String?): Drawable = TODO()

    override fun extendVerificationTimeout(id: Int, verificationCodeAtTimeout: Int, millisecondsToDelay: Long) = TODO()

    override fun getText(packageName: String?, resid: Int, appInfo: ApplicationInfo?): CharSequence = TODO()

    override fun resolveContentProvider(name: String?, flags: Int): ProviderInfo = TODO()

    override fun getApplicationEnabledSetting(packageName: String?): Int = TODO()

    override fun queryIntentServices(intent: Intent?, flags: Int): MutableList<ResolveInfo> = TODO()

    override fun hasSystemFeature(name: String?): Boolean = TODO()

    override fun hasSystemFeature(name: String?, version: Int): Boolean = TODO()

    override fun getInstrumentationInfo(className: ComponentName?, flags: Int): InstrumentationInfo = TODO()

    override fun getInstalledApplications(flags: Int): MutableList<ApplicationInfo> = TODO()

    override fun isPermissionRevokedByPolicy(permName: String, pkgName: String): Boolean = TODO()

    override fun getUserBadgedDrawableForDensity(drawable: Drawable?, user: UserHandle?, badgeLocation: Rect?, badgeDensity: Int): Drawable = TODO()

    override fun checkPermission(permName: String?, pkgName: String?): Int = TODO()

    override fun getInstantAppCookieMaxBytes(): Int = TODO()

    override fun getDefaultActivityIcon(): Drawable = TODO()

    override fun getPreferredPackages(flags: Int): MutableList<PackageInfo> = TODO()

    override fun checkSignatures(pkg1: String?, pkg2: String?): Int = TODO()

    override fun checkSignatures(uid1: Int, uid2: Int): Int = TODO()

    override fun addPreferredActivity(filter: IntentFilter?, match: Int, set: Array<out ComponentName>?, activity: ComponentName?) = TODO()

    override fun removePackageFromPreferred(packageName: String?) = TODO()

    override fun getSharedLibraries(flags: Int): MutableList<SharedLibraryInfo> = TODO()

    override fun queryIntentActivities(intent: Intent?, flags: Int): MutableList<ResolveInfo> =
        queryIntentActivitiesResult

    override fun addPermission(info: PermissionInfo?): Boolean = TODO()

    override fun getActivityBanner(activityName: ComponentName?): Drawable = TODO()

    override fun getActivityBanner(intent: Intent?): Drawable = TODO()

    override fun getDrawable(packageName: String?, resid: Int, appInfo: ApplicationInfo?): Drawable = TODO()

    override fun setComponentEnabledSetting(componentName: ComponentName, newState: Int, flags: Int) = TODO()

    override fun getChangedPackages(sequenceNumber: Int): ChangedPackages? = TODO()

    override fun getApplicationInfo(packageName: String?, flags: Int): ApplicationInfo = TODO()

    override fun resolveActivity(intent: Intent?, flags: Int): ResolveInfo = TODO()

    override fun queryBroadcastReceivers(intent: Intent?, flags: Int): MutableList<ResolveInfo> = TODO()

    override fun getXml(packageName: String?, resid: Int, appInfo: ApplicationInfo?): XmlResourceParser = TODO()

    override fun getPackageInfo(packageName: String?, flags: Int): PackageInfo = TODO()

    override fun getPackageInfo(versionedPackage: VersionedPackage?, flags: Int): PackageInfo = TODO()

    override fun getPackagesHoldingPermissions(permissions: Array<out String>?, flags: Int): MutableList<PackageInfo> = TODO()

    override fun addPermissionAsync(info: PermissionInfo?): Boolean = TODO()

    override fun getSystemAvailableFeatures(): Array<FeatureInfo> = TODO()

    override fun getActivityLogo(activityName: ComponentName?): Drawable = TODO()

    override fun getActivityLogo(intent: Intent?): Drawable = TODO()

    override fun getSystemSharedLibraryNames(): Array<String> = TODO()

    override fun queryPermissionsByGroup(group: String?, flags: Int): MutableList<PermissionInfo> = TODO()

    override fun queryIntentContentProviders(intent: Intent?, flags: Int): MutableList<ResolveInfo> = TODO()

    override fun getApplicationBanner(info: ApplicationInfo?): Drawable = TODO()

    override fun getApplicationBanner(packageName: String?): Drawable = TODO()

    override fun queryContentProviders(processName: String?, uid: Int, flags: Int): MutableList<ProviderInfo> = TODO()

    override fun getPackageGids(packageName: String): IntArray = TODO()

    override fun getPackageGids(packageName: String?, flags: Int): IntArray = TODO()

    override fun getResourcesForActivity(activityName: ComponentName?): Resources = TODO()

    override fun getPackagesForUid(uid: Int): Array<String>? = TODO()

    override fun getPermissionGroupInfo(name: String?, flags: Int): PermissionGroupInfo = TODO()

    override fun getPermissionInfo(name: String?, flags: Int): PermissionInfo = TODO()

    override fun removePermission(name: String?) = TODO()

    override fun queryInstrumentation(targetPackage: String?, flags: Int): MutableList<InstrumentationInfo> = TODO()

    override fun clearInstantAppCookie() = TODO()

    override fun addPackageToPreferred(packageName: String?) = TODO()

    override fun currentToCanonicalPackageNames(names: Array<out String>?): Array<String> = TODO()

    override fun getPackageUid(packageName: String?, flags: Int): Int = TODO()

    override fun getComponentEnabledSetting(componentName: ComponentName?): Int = TODO()

    override fun getLeanbackLaunchIntentForPackage(packageName: String): Intent? = TODO()

    override fun getInstalledPackages(flags: Int): MutableList<PackageInfo> = TODO()

    override fun getUserBadgedIcon(icon: Drawable?, user: UserHandle?): Drawable = TODO()

    override fun getAllPermissionGroups(flags: Int): MutableList<PermissionGroupInfo> = TODO()

    override fun getActivityInfo(component: ComponentName?, flags: Int): ActivityInfo = TODO()

    override fun getNameForUid(uid: Int): String? = TODO()

    override fun updateInstantAppCookie(cookie: ByteArray?) = TODO()

    override fun getApplicationLogo(info: ApplicationInfo?): Drawable = TODO()

    override fun getApplicationLogo(packageName: String?): Drawable = TODO()

    override fun getApplicationLabel(info: ApplicationInfo?): CharSequence = TODO()

    override fun getPreferredActivities(outFilters: MutableList<IntentFilter>, outActivities: MutableList<ComponentName>, packageName: String?): Int = TODO()

    override fun setApplicationCategoryHint(packageName: String, categoryHint: Int) = TODO()

    override fun isSafeMode(): Boolean = TODO()

    override fun setInstallerPackageName(targetPackage: String?, installerPackageName: String?) = TODO()

    override fun getUserBadgedLabel(label: CharSequence?, user: UserHandle?): CharSequence = TODO()

    override fun getInstallerPackageName(packageName: String?): String = TODO()

    override fun setApplicationEnabledSetting(packageName: String, newState: Int, flags: Int) = TODO()

    override fun canRequestPackageInstalls(): Boolean = TODO()

    override fun getServiceInfo(component: ComponentName?, flags: Int): ServiceInfo = TODO()

    override fun isInstantApp(): Boolean = TODO()

    override fun isInstantApp(packageName: String?): Boolean = TODO()

    override fun getActivityIcon(activityName: ComponentName?): Drawable = TODO()

    override fun getActivityIcon(intent: Intent?): Drawable = TODO()

}