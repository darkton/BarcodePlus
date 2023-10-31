package br.com.harkin.barcodeplus.listener;

import static com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate.InstallState.STATE_CANCELED;
import static com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate.InstallState.STATE_COMPLETED;
import static com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate.InstallState.STATE_FAILED;

import androidx.appcompat.app.AlertDialog;
import android.widget.ProgressBar;

import com.google.android.gms.common.moduleinstall.InstallStatusListener;
import com.google.android.gms.common.moduleinstall.ModuleInstallClient;
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate;

public final class ModuleInstallProgressListener implements InstallStatusListener {
    private AlertDialog alertDialog;
    private ModuleInstallClient moduleInstallClient;
    private ProgressBar progressBar;

    public ModuleInstallProgressListener(AlertDialog alertDialog, ModuleInstallClient moduleInstallClient, ProgressBar progressBar) {
        this.alertDialog = alertDialog;
        this.moduleInstallClient = moduleInstallClient;
        this.progressBar = progressBar;
    }

    @Override
    public void onInstallStatusUpdated(ModuleInstallStatusUpdate update) {
        ModuleInstallStatusUpdate.ProgressInfo progressInfo = update.getProgressInfo();
        if (progressInfo != null) {
            int progress = (int) (progressInfo.getBytesDownloaded() * 100 / progressInfo.getTotalBytesToDownload());

            progressBar.setProgress(progress);
        }

        if (isTerminateState(update.getInstallState())) {
            moduleInstallClient.unregisterListener(this);
            alertDialog.dismiss();
        }
    }

    public boolean isTerminateState(@ModuleInstallStatusUpdate.InstallState int state) {
        return state == STATE_CANCELED || state == STATE_COMPLETED || state == STATE_FAILED;
    }
}