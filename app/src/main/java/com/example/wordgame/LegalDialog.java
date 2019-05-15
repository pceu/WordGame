package com.example.wordgame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

public class LegalDialog extends AppCompatDialogFragment {
    @Override
    // In dialog, can see the text message with license
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Legal License")
                .setMessage("<wordgame.png> CoolGames b.v. CC0\n" +
                        "http://www.wordgames.com/en/puzzle/\n" +
                        "\n" +
                        "<coin.png> galangpiliang CC0\n" +
                        "https://opengameart.org/content/coin-icon\n" +
                        "\n" +
                        "<splash.png> Richardson CC0\n" +
                        "https://www.peoplefun.com/\n" +
                        "\n" +
                        "<hint_button> Ahkam CC0\n" +
                        "https://www.freeiconspng.com/img/820\n" +
                        "\n" +
                        "<backgroundmusic.mp3> WP Weaver CC0\n" +
                        "https://soundimage.org/puzzle-music-2/")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder.create();
    }
}
