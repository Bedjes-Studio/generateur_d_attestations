package com.studio.bedjes.generateur_dattestations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import crl.android.pdfwriter.PDFWriter;
import crl.android.pdfwriter.PaperSize;
import crl.android.pdfwriter.StandardFonts;

public class MainActivity extends AppCompatActivity {

    DatePickerDialog datePicker;

    EditText editTextPrenom;
    EditText editTextNom;
    EditText editTextDateNaissance;
    EditText editTextLieuNaissance;
    EditText editTextAdresse;
    EditText editTextVille;
    EditText editTextCodePostal;
    EditText editTextHeureSortie;
    EditText editTextDateSortie;


    CheckBox[] boxMotifs = new CheckBox[9];

    TextView textResult;
    TextView textBJS;
ImageView logoBJS;

    Button buttonSauver;

    Button buttonMotif;
    Button buttonInfos;
    Button buttonCreation;
    Button buttonGenerer;
    Button buttonOuvir;

String prenom ="";
String nom = "";
String dateNaissance ="";
String lieuNaissance = "";
String adresse = "";
String ville = "";
String codePostal = "";
String dateSortie = "";
String heureSortie = "";

int[] motifs = new int[9];
String motif = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          loadInfosScreen();
          chargerProfil(this);
    }

    public boolean estRempli(int menu) {

        switch (menu) {
            case 0 : // infos
                if (!prenom.equals("") && !nom.equals("") && !dateNaissance.equals("") && !lieuNaissance.equals("") && !adresse.equals("") && !ville.equals("") && !codePostal.equals("") && !dateSortie.equals("") && !heureSortie.equals("")) {
                    return true;
                }
                break;
            case 1 : // motif
                int s = 0;
                for (int i = 0; i < 9; ++i) {
                    s += motifs[i];
                }
                if (s > 0) {
                    return true;
                }
                break;
        }
        return false;
    }

    public void loadInfosScreen() {
    setContentView(R.layout.infos_layout);

    editTextPrenom = findViewById(R.id.editTextPrenom);
    editTextNom= findViewById(R.id.editTextNom);
    initialiserDateNaissance();
    editTextLieuNaissance = findViewById(R.id.editTextLieuNaissance);
    editTextAdresse = findViewById(R.id.editTextAdresse);
    editTextVille = findViewById(R.id.editTextVille);
    editTextCodePostal = findViewById(R.id.editTextCodePostal);
    initialiserDateSortie();
    initialiserHeureSortie();

    reloadInfosScreen();

        buttonSauver = findViewById(R.id.buttonSauver);
        buttonSauver.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               enregistrerLesInformations();
                                           }
                                       }


        );

    buttonMotif = findViewById(R.id.buttonMotif);
    buttonMotif.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           saveInfosScreen();
                                           if (estRempli(0)) {
    loadMotifScreen();
} else {
    Toast.makeText(MainActivity.this, "Merci de remplir toutes vos informations", Toast.LENGTH_SHORT).show();
}
                                       }
                                   }


    );

}
    public void saveInfosScreen() {
    prenom = editTextPrenom.getText().toString();
    nom = editTextNom.getText().toString();
    dateNaissance = editTextDateNaissance.getText().toString();
    lieuNaissance = editTextLieuNaissance.getText().toString();
    adresse = editTextAdresse.getText().toString();
    ville = editTextVille.getText().toString();
    codePostal = editTextCodePostal.getText().toString();
    dateSortie = editTextDateSortie.getText().toString();
    heureSortie = editTextHeureSortie.getText().toString();
}
    public void reloadInfosScreen() {
    editTextPrenom.setText(prenom);
    editTextNom.setText(nom);
    editTextDateNaissance.setText(dateNaissance);
    editTextLieuNaissance.setText(lieuNaissance);
    editTextAdresse.setText(adresse);
    editTextVille.setText(ville);
    editTextCodePostal.setText(codePostal);
    editTextDateSortie.setText(dateSortie);
    editTextHeureSortie.setText(heureSortie);
}

    public void loadMotifScreen() {
    setContentView(R.layout.modif_layout);


        boxMotifs[0] =  findViewById(R.id.editTextMotif1);
        boxMotifs[1] =  findViewById(R.id.editTextMotif2);
        boxMotifs[2] =  findViewById(R.id.editTextMotif3);
        boxMotifs[3] =  findViewById(R.id.editTextMotif4);
        boxMotifs[4] =  findViewById(R.id.editTextMotif5);
        boxMotifs[5] =  findViewById(R.id.editTextMotif6);
        boxMotifs[6] =  findViewById(R.id.editTextMotif7);
        boxMotifs[7] =  findViewById(R.id.editTextMotif8);
        boxMotifs[8] =  findViewById(R.id.editTextMotif9);


        reloadMotifScreen();

        buttonInfos = findViewById(R.id.buttonInfos);
    buttonInfos.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           saveMotifsScreen();
                                           loadInfosScreen();

                                       }
                                   }
    );

    buttonCreation = findViewById(R.id.buttonCreation);
    buttonCreation.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              saveMotifsScreen();
                                              if (estRempli(1)) {
                                            //  loadCreationScreen();

                                                  Toast.makeText(MainActivity.this, "Création de l'attestation en cours...", Toast.LENGTH_SHORT).show();

                                                  new Thread(new Runnable() {
                                                      public void run() {

                                                          genererPDF();

                                                      }
                                                  }).start();
                                              } else {
                                                  Toast.makeText(MainActivity.this, "Merci de choisi au moins 1 motif", Toast.LENGTH_SHORT).show();
                                              }
                                          }
                                      }

    );
}
    public void saveMotifsScreen() {
      for(int i = 0; i < 9; ++i ) {
         if (boxMotifs[i].isChecked() ) {
          motifs[i] = 1;
         } else {
             motifs[i] = 0;
         }
      }
    }
    public void reloadMotifScreen() {
        for(int i = 0; i < 9; ++i ) {
            if (motifs[i] == 1 ) {
                boxMotifs[i].setChecked(true);
            } else {
                boxMotifs[i].setChecked(false);
            }
        }
    }

    public void loadCreationScreen() {
    setContentView(R.layout.creation_layout);

    textResult = findViewById(R.id.textResult);
        textBJS = findViewById(R.id.textViewBJS);
        logoBJS = findViewById(R.id.logoBJS);

    buttonMotif = findViewById(R.id.buttonMotif);
    buttonMotif.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           loadMotifScreen();
                                       }
                                   }
    );

        buttonOuvir = findViewById(R.id.buttonOuvir);
        buttonOuvir.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               File myFile = new File( getExternalFilesDir(null), genererNomFichier());

                                               Intent intent = new Intent(Intent.ACTION_VIEW);
                                               intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                               Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", myFile);

                                               intent.setDataAndType(photoURI, "application/pdf");

                                               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                               startActivity(intent);
                                           }
                                       }
        );
}

    public void initialiserDateNaissance() {
        editTextDateNaissance = findViewById(R.id.editTextDateNaissance);
        editTextDateNaissance.setInputType(InputType.TYPE_NULL);
        editTextDateNaissance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDatePicker1();
            }
        });
    }
    public void showCustomDatePicker1()  {

        Calendar dateCalendar = Calendar.getInstance();
        int day = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int month = dateCalendar.get(Calendar.MONTH);
        int year = dateCalendar.get(Calendar.YEAR);

       datePicker = new DatePickerDialog(MainActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar,  new DatePickerDialog.OnDateSetListener() {
           @Override
           public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
               editTextDateNaissance.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
           }
       }, year, month, day);
        datePicker.setTitle("Saisissez une date");
        datePicker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        datePicker.show();
    }

    public void initialiserDateSortie() {
        editTextDateSortie=findViewById(R.id.editTextDateSortie);
        editTextDateSortie.setInputType(InputType.TYPE_NULL);
        editTextDateSortie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDatePicker2();
            }
        });
    }
    public void showCustomDatePicker2()  {

         Calendar dateCalendar = Calendar.getInstance();
        int day = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int month = dateCalendar.get(Calendar.MONTH);
        int year = dateCalendar.get(Calendar.YEAR);
        datePicker = new DatePickerDialog(MainActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar,  new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editTextDateSortie.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        }, year, month, day);
        datePicker.setTitle("Saisissez une date");
        datePicker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        datePicker.show();
    }

    public void initialiserHeureSortie() {
        editTextHeureSortie=findViewById(R.id.editTextHeureSortie);
        editTextHeureSortie.setInputType(InputType.TYPE_NULL);
        editTextHeureSortie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomTimePicker1();
            }
        });
    }
    public void showCustomTimePicker1()  {
        Calendar timeCalender = Calendar.getInstance();
        int hour = timeCalender.get(Calendar.HOUR_OF_DAY);
        int minute = timeCalender.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (minute < 10) {
                    editTextHeureSortie.setText(hourOfDay + ":0" + minute);
                } else {
                    editTextHeureSortie.setText(hourOfDay + ":" + minute);

                }
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
        timePickerDialog.setTitle("Saisissez l'heure");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }

    public void enregistrerLesInformations() {
        prenom = editTextPrenom.getText().toString();
        nom = editTextNom.getText().toString();
        dateNaissance = editTextDateNaissance.getText().toString();
        lieuNaissance = editTextLieuNaissance.getText().toString();
        adresse = editTextAdresse.getText().toString();
        ville = editTextVille.getText().toString();
        codePostal = editTextCodePostal.getText().toString();


        String mystring = prenom + ";" + nom + ";" + dateNaissance + ";" + lieuNaissance + ";" + adresse + ";" + ville + ";" + codePostal;

        try {

            FileOutputStream infos = openFileOutput("profil.txt", MODE_PRIVATE);
            infos.write(mystring.getBytes());
            infos.close();

            Toast.makeText(MainActivity.this, "Le profil à été sauvegardé", Toast.LENGTH_SHORT).show();


        }
        catch (IOException e) {
            Toast.makeText(MainActivity.this, "Désolé, une erreur est survenue, code erreur : IFEI32", Toast.LENGTH_SHORT).show();
        }


    }

    private void chargerProfil(Context context) {

        String[] profil;

        try {
            InputStream inputStream = context.openFileInput("profil.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                receiveString = bufferedReader.readLine();
                stringBuilder.append(receiveString);


                inputStream.close();
                profil = stringBuilder.toString().split(";");

                String s = ":";
                for (int i = 0; i < profil.length; ++i) {
                    s += profil[i];
                }
                Log.i("HUGO",s );


                if ( profil.length > 0) {
                    prenom = profil[0];
                }
                if ( profil.length > 1) {
                    nom = profil[1];
                }
                if ( profil.length > 2) {
                    dateNaissance = profil[2];
                }
                if ( profil.length > 3) {
                    lieuNaissance = profil[3];
                }
                if ( profil.length > 4) {
                    adresse = profil[4];
                }
                if ( profil.length > 5) {
                    ville = profil[5];
                }
                if ( profil.length > 6) {
                    codePostal = profil[6];
                }
            }
        }
        catch (FileNotFoundException e) {
            Log.e("HUGO", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("HUGO", "Can not read file: " + e.toString());
        }

        reloadInfosScreen();
    }

    public Bitmap genererQRCode(int size) {

// this is a small sample use of the QRCodeEncoder class from zxing
        QRCodeWriter writer = new QRCodeWriter();

        Bitmap bmp = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);

        String[] heureCreationSplit = heureSortie.split(":");

        String text = "Cree le: " + dateSortie + " a " + heureCreationSplit[0] + "h" + heureCreationSplit[1] + ";" +
                "\nNom: " + nom + ";" +
                "\nPrenom: " + prenom + ";" +
                "\nNaissance: " + dateNaissance + ";" +
                "\nAdresse: " + adresse + ";" +
                "\nSortie: " + dateSortie + " a " + heureSortie + ";" +
                "\nMotif : " + motif;
        try {
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bmp;
    }

    public void cocherCases(PDFWriter writer) {

        if ( motifs[0] == 1) {

            writer.addLine(78,625,98,645);
            writer.addLine(78,645,98,625);

            motif += "travail, ";
        }

        if ( motifs[1] == 1) {

            writer.addLine(78,545,98,565);
            writer.addLine(78,565,98,545);

            motif += "achats, ";

        }

        if ( motifs[2] == 1) {

            writer.addLine(78,465,98,485);
            writer.addLine(78,485,98,465);

            motif += "sante, ";

        }

        if ( motifs[3] == 1) {

            writer.addLine(78,425,98,445);
            writer.addLine(78,445,98,425);

            motif += "famille, ";

        }

        if ( motifs[4] == 1) {

            writer.addLine(78,387,98,407);
            writer.addLine(78,407,98,387);

            motif += "handicap, ";

        }

        if ( motifs[5] == 1) {

            writer.addLine(78,360,98,380);
            writer.addLine(78,380,98,360);

            motif += "sport_animaux, ";

        }

        if ( motifs[6] == 1) {

            writer.addLine(78,277,98,297);
            writer.addLine(78,297,98,277);

            motif += "convocation, ";

        }

        if ( motifs[7] == 1) {

            writer.addLine(78,252,98,272);
            writer.addLine(78,272,98,252);

            motif += "missions, ";

        }

        if ( motifs[8] == 1) {

            writer.addLine(78,225,98,245);
            writer.addLine(78,245,98,225);

            motif += "enfants, ";

        }


    }

public void genererPDF() {

        // création du pdf
    PDFWriter writer = new PDFWriter(PaperSize.FOLIO_WIDTH, PaperSize.FOLIO_HEIGHT);
// saisie de la font
    writer.setFont(StandardFonts.TIMES_ROMAN, StandardFonts.TIMES_ROMAN, StandardFonts.WIN_ANSI_ENCODING);

    writer.addText(100, 870, 17, "ATTESTATION DE DÉPLACEMENT DÉROGATOIRE");
    writer.addText(90, 845, 11, " En application du décret n°2020-1310 du 29 octobre 2020 prescrivant les mesures générales ");
    writer.addText(90, 835, 11, "nécessaires pour faire face à l'épidémie de covid-19 dans le cadre de l'état d'urgence sanitaire");


    writer.addText(75, 805, 12, "Je soussigné(e),");
    writer.addText(75, 785, 12, "Mme/M. : " + prenom + " " + nom);
    writer.addText(75, 765, 12, "Né(e) le : " + dateNaissance);
    writer.addText(300, 765, 12, "à : " + lieuNaissance);
    writer.addText(75, 745, 12, "Demerant : " + adresse + " " + codePostal + " " + ville);
    writer.addText(75, 725, 12, "certifie que mon déplacement est lié au motif suivant (cocher la case) autorisé par le décret");
    writer.addText(75, 710, 12, "n°2020-1310 du 29 octobre 2020 prescrivant les mesures générales nécessaires pour faire face à");
    writer.addText(75, 695, 12, "l'épidémie de covid-19 dans le cadre de l'état d'urgence sanitaire :");

    writer.addText(75, 680, 10, "Note : les personnes souhaitant bénéficier de l'une de ces exceptions doivent se munir s'il y a lieu, lors de leurs");
    writer.addText(75, 670, 10, "déplacements hors de leur domicile, d'un document leur permettant de justifier que le déplacement considéré entre");
    writer.addText(75, 660, 10, "dans le champ de l'une de ces exceptions.");

    writer.addRectangle(78, 625,20, 20);
    writer.addText(110, 635, 12, "1. Déplacements entre le domicile et le lieu d'exercice de l'activité professionnelle ou un");
    writer.addText(110, 620, 12, "établissement d'enseignement ou de formation, déplacements professionnels ne pouvant");
    writer.addText(110, 605, 12, "être différés, déplacements pour un concours ou un examen. ");

    writer.addText(110, 590, 10, "Note : à utiliser par les travailleurs non-salariés, lorsqu'ils ne peuvent disposer d'un justificatif de déplacement");
    writer.addText(110, 580, 10, " établi par leur employeur.");

    writer.addRectangle(78, 545,20, 20);
    writer.addText(110, 555, 12, "2. Déplacements pour effectuer des achats de fournitures nécessaires à l'activité");
    writer.addText(110, 540, 12, "professionnelle, des achats de première nécessité³ dans des établissements dont les");
    writer.addText(110, 525, 12, "activités demeurent autorisées, le retrait de commande et les livraisons à domicile.");

    writer.addText(110, 510, 10, "Note : achats de première nécessité y compris les acquisitions à titre gratuit (distribution de denrées alimentaires...)");
    writer.addText(110, 500, 10, "et les déplacements liés à la perception de prestations sociales et au retrait d'espèces.");

    writer.addRectangle(78, 465,20, 20);
    writer.addText(110, 475, 12, "3. Consultations, médicaments. examens et  soins ne   pouvant  être  assurés  à  distance  et l'achat");
    writer.addText(110, 460, 12, "de médicaments");

    writer.addRectangle(78, 425,20, 20);
    writer.addText(110, 435, 12, "4. Déplacements pour motif familial impérieux, pour l'assistance aux personnes vulnérables ");
    writer.addText(110, 420, 12, "et précaires ou la garde d'enfants.");

    writer.addRectangle(78, 387,20, 20);
    writer.addText(110, 395, 12, "5. Déplacement des personnes en situation de handicap et leur accompagnant.");

    writer.addRectangle(78, 360,20, 20);
    writer.addText(110, 370, 12, "6. Déplacements brefs, dans la limite d'une heure quotidienne et dans un rayon maximal");
    writer.addText(110, 355, 12, "d'un  kilomètre  autour  du  domicile,  liés  soit  à  l'activité  physique  individuelle  des");
    writer.addText(110, 340, 12, "personnes, à l'exclusion de toute pratique sportive collective et de toute proximité avec");
    writer.addText(110, 325, 12, "d'autres personnes, soit à la promenade avec les seules personnes regroupées dans un ");
    writer.addText(110, 310, 12, "même domicile, soit aux besoins des animaux de compagnie.");

    writer.addRectangle(78, 277,20, 20);
    writer.addText(110, 285, 12, "7. Convocation judiciaire ou administrative et pour se rendre dans un service public");

    writer.addRectangle(78, 252,20, 20);
    writer.addText(110, 260, 12, "8. Participation à des missions d'intérêt général sur demande de l'autorité administrative ");

    writer.addRectangle(78, 225,20, 20);
    writer.addText(110, 235, 12, "9. Déplacement  pour  chercher  les  enfants  à  l'école  et  à  l’occasion  de  leurs  activités ");
    writer.addText(110, 220, 12, "périscolaires");

    writer.addText(75, 165, 12, "Fait à : " + ville);
    writer.addText(75, 140, 12, "Le : " + dateSortie);
    writer.addText(300, 140, 12, "à : " + heureSortie);
    writer.addText(75, 115, 12, "(Date et heure de début de sortie à mentionner obligatoirement)");

    writer.addText(75, 90, 12, "Signature : ");

    writer.addImage(395,55, genererQRCode(150));

    cocherCases(writer);

    writer.newPage();
    writer.addImage(50,450, genererQRCode(450));

    // exportation du fichier
    outputToFile(genererNomFichier(), writer.asString(), "ISO-8859-1");
}

public String genererNomFichier() {
        String[] dateSortieSplit = dateSortie.split("/");
        String[] heureCreationSplit = heureSortie.split(":");

        return "attestation-" + dateSortieSplit[2] + "-" + dateSortieSplit[1] + "-" + dateSortieSplit[0] + "_" + heureCreationSplit[0] + "-" + heureCreationSplit[1] + ".pdf";
}

    public void outputToFile(final String fileName, final String pdfContent, final String encoding) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {


                    try {


                        File my_file = new File(getExternalFilesDir(null), fileName);
                        FileOutputStream pdfFile = new FileOutputStream(my_file);

                        pdfFile.write(pdfContent.getBytes(encoding));
                        pdfFile.close();

                        loadCreationScreen();

                        textResult.setText("Le fichier pdf à bien été créé dans  \n" + getExternalFilesDir(null)  + fileName);

                        buttonOuvir.setVisibility(View.VISIBLE);
                        textBJS.setVisibility(View.VISIBLE);
                        logoBJS.setVisibility(View.VISIBLE);


                    } catch (FileNotFoundException | UnsupportedEncodingException e) {
                    textResult.setText("Désolé, une erreur est survenue : " + e.getMessage());
                } catch (IOException e) {
                        textResult.setText("Désolé, une erreur est survenue : " + e.getMessage());
                    }
            }});
    }
}