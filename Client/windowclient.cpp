#include "windowclient.h"
#include "ui_windowclient.h"
#include <QMessageBox>
#include <string>
#include "../Serveur/Tcp.h"
using namespace std;

extern WindowClient *w;

typedef struct
{
  int   id;
  char  intitule[50];
  double prix;
  int   quantite;  
} ARTICLEPANIER;

int sClient;
void HandlerSIGINT(int s);

void Echange(char* requete, char* reponse);
bool SMOP_Login(const char* user,const char* password);
void SMOP_Logout();

int numArticle = 1;
ARTICLEPANIER tabPanierClient[20];




#define REPERTOIRE_IMAGES "Client/images/"

WindowClient::WindowClient(QWidget *parent) : QMainWindow(parent), ui(new Ui::WindowClient)
{
    ui->setupUi(this);

    // Configuration de la table du panier (ne pas modifer)
    ui->tableWidgetPanier->setColumnCount(3);
    ui->tableWidgetPanier->setRowCount(0);
    QStringList labelsTablePanier;
    labelsTablePanier << "Article" << "Prix à l'unité" << "Quantité";
    ui->tableWidgetPanier->setHorizontalHeaderLabels(labelsTablePanier);
    ui->tableWidgetPanier->setSelectionMode(QAbstractItemView::SingleSelection);
    ui->tableWidgetPanier->setSelectionBehavior(QAbstractItemView::SelectRows);
    ui->tableWidgetPanier->horizontalHeader()->setVisible(true);
    ui->tableWidgetPanier->horizontalHeader()->setDefaultSectionSize(160);
    ui->tableWidgetPanier->horizontalHeader()->setStretchLastSection(true);
    ui->tableWidgetPanier->verticalHeader()->setVisible(false);
    ui->tableWidgetPanier->horizontalHeader()->setStyleSheet("background-color: lightyellow");

    ui->pushButtonPayer->setText("Confirmer achat");
    setPublicite("!!! Bienvenue sur le Maraicher en ligne !!!");

    // Armement des signaux
    struct sigaction A;
    A.sa_flags = 0;
    sigemptyset(&A.sa_mask);
    A.sa_handler = HandlerSIGINT;
    if (sigaction(SIGINT,&A,NULL) == -1)
    {
        perror("Erreur de sigaction");
        exit(1);
    }
    //Connexion au serveur
    //ici le premier argument (ipServeur) est a NULL donc
    // donc nous sommes en local mais si on veut connecter deux
    // machines distantes on doit rentrer l'ip 
    //char ip[NI_MAXHOST] = "";
    sClient = ClientSocket(NULL,5678);
    printf("Connecte sur le serveur.\n");



    // Exemples à supprimer
    //setArticle("pommes",5.53,18,"pommes.jpg");
    //ajouteArticleTablePanier("cerises",8.96,2);
}

WindowClient::~WindowClient()
{
    delete ui;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions utiles : ne pas modifier /////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setNom(const char* Text)
{
  if (strlen(Text) == 0 )
  {
    ui->lineEditNom->clear();
    return;
  }
  ui->lineEditNom->setText(Text);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
const char* WindowClient::getNom()
{
  strcpy(nom,ui->lineEditNom->text().toStdString().c_str());
  return nom;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setMotDePasse(const char* Text)
{
  if (strlen(Text) == 0 )
  {
    ui->lineEditMotDePasse->clear();
    return;
  }
  ui->lineEditMotDePasse->setText(Text);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
const char* WindowClient::getMotDePasse()
{
  strcpy(motDePasse,ui->lineEditMotDePasse->text().toStdString().c_str());
  return motDePasse;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setPublicite(const char* Text)
{
  if (strlen(Text) == 0 )
  {
    ui->lineEditPublicite->clear();
    return;
  }
  ui->lineEditPublicite->setText(Text);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setImage(const char* image)
{
  // Met à jour l'image
  char cheminComplet[80];
  sprintf(cheminComplet,"%s%s",REPERTOIRE_IMAGES,image);
  QLabel* label = new QLabel();
  label->setSizePolicy(QSizePolicy::Ignored, QSizePolicy::Ignored);
  label->setScaledContents(true);
  QPixmap *pixmap_img = new QPixmap(cheminComplet);
  label->setPixmap(*pixmap_img);
  label->resize(label->pixmap()->size());
  ui->scrollArea->setWidget(label);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
int WindowClient::isNouveauClientChecked()
{
  if (ui->checkBoxNouveauClient->isChecked()) return 1;
  return 0;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setArticle(const char* intitule,float prix,int stock,const char* image)
{
  ui->lineEditArticle->setText(intitule);
  if (prix >= 0.0)
  {
    char Prix[20];
    sprintf(Prix,"%.2f",prix);
    ui->lineEditPrixUnitaire->setText(Prix);
  }
  else ui->lineEditPrixUnitaire->clear();
  if (stock >= 0)
  {
    char Stock[20];
    sprintf(Stock,"%d",stock);
    ui->lineEditStock->setText(Stock);
  }
  else ui->lineEditStock->clear();
  setImage(image);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
int WindowClient::getQuantite()
{
  return ui->spinBoxQuantite->value();
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setTotal(float total)
{
  if (total >= 0.0)
  {
    char Total[20];
    sprintf(Total,"%.2f",total);
    ui->lineEditTotal->setText(Total);
  }
  else ui->lineEditTotal->clear();
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::loginOK()
{
  ui->pushButtonLogin->setEnabled(false);
  ui->pushButtonLogout->setEnabled(true);
  ui->lineEditNom->setReadOnly(true);
  ui->lineEditMotDePasse->setReadOnly(true);
  ui->checkBoxNouveauClient->setEnabled(false);

  ui->spinBoxQuantite->setEnabled(true);
  ui->pushButtonPrecedent->setEnabled(true);
  ui->pushButtonSuivant->setEnabled(true);
  ui->pushButtonAcheter->setEnabled(true);
  ui->pushButtonSupprimer->setEnabled(true);
  ui->pushButtonViderPanier->setEnabled(true);
  ui->pushButtonPayer->setEnabled(true);

  ui->lineEditNom->setDisabled(true);
  ui->lineEditMotDePasse->setDisabled(true);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::logoutOK()
{
  ui->pushButtonLogin->setEnabled(true);
  ui->pushButtonLogout->setEnabled(false);
  ui->lineEditNom->setReadOnly(false);
  ui->lineEditMotDePasse->setReadOnly(false);
  ui->checkBoxNouveauClient->setEnabled(true);

  ui->spinBoxQuantite->setEnabled(false);
  ui->pushButtonPrecedent->setEnabled(false);
  ui->pushButtonSuivant->setEnabled(false);
  ui->pushButtonAcheter->setEnabled(false);
  ui->pushButtonSupprimer->setEnabled(false);
  ui->pushButtonViderPanier->setEnabled(false);
  ui->pushButtonPayer->setEnabled(false);

  setNom("");
  setMotDePasse("");
  ui->checkBoxNouveauClient->setCheckState(Qt::CheckState::Unchecked);

  setArticle("",-1.0,-1,"");

  w->videTablePanier();
  w->setTotal(-1.0);

  ui->lineEditNom->setEnabled(true);
  ui->lineEditMotDePasse->setEnabled(true);

}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions utiles Table du panier (ne pas modifier) /////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::ajouteArticleTablePanier(const char* article,float prix,int quantite)
{
    char Prix[20],Quantite[20];

    sprintf(Prix,"%.2f",prix);
    sprintf(Quantite,"%d",quantite);

    // Ajout possible
    int nbLignes = ui->tableWidgetPanier->rowCount();
    nbLignes++;
    ui->tableWidgetPanier->setRowCount(nbLignes);
    ui->tableWidgetPanier->setRowHeight(nbLignes-1,10);

    QTableWidgetItem *item = new QTableWidgetItem;
    item->setFlags(Qt::ItemIsSelectable|Qt::ItemIsEnabled);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(article);
    ui->tableWidgetPanier->setItem(nbLignes-1,0,item);

    item = new QTableWidgetItem;
    item->setFlags(Qt::ItemIsSelectable|Qt::ItemIsEnabled);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(Prix);
    ui->tableWidgetPanier->setItem(nbLignes-1,1,item);

    item = new QTableWidgetItem;
    item->setFlags(Qt::ItemIsSelectable|Qt::ItemIsEnabled);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(Quantite);
    ui->tableWidgetPanier->setItem(nbLignes-1,2,item);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::videTablePanier()
{
    ui->tableWidgetPanier->setRowCount(0);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
int WindowClient::getIndiceArticleSelectionne()
{
    QModelIndexList liste = ui->tableWidgetPanier->selectionModel()->selectedRows();
    if (liste.size() == 0) return -1;
    QModelIndex index = liste.at(0);
    int indice = index.row();
    return indice;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions permettant d'afficher des boites de dialogue (ne pas modifier ////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::dialogueMessage(const char* titre,const char* message)
{
   QMessageBox::information(this,titre,message);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::dialogueErreur(const char* titre,const char* message)
{
   QMessageBox::critical(this,titre,message);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////// CLIC SUR LA CROIX DE LA FENETRE /////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::closeEvent(QCloseEvent *event)
{

  exit(0);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions clics sur les boutons ////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonLogin_clicked()
{
  char messageRecu[1400];
  char messageEnvoye[1400];
  int newClient = 0;
  if(isNouveauClientChecked())
    newClient = 1;
  sprintf(messageEnvoye, "LOGIN#%s#%s#%d", getNom(), getMotDePasse(),newClient);

  Echange(messageEnvoye, messageRecu);
   if (strcmp(messageRecu, "LOGIN#ko#pwd") == 0) {
        dialogueErreur("Erreur d'authentification", "Mauvais mot de passe !");
    } else if (strcmp(messageRecu, "LOGIN#ko#username") == 0) {
          dialogueErreur("Erreur d'authentification", "Mauvais identifiants !");
    } else {
        if (strcmp(messageRecu, "LOGIN#ok") == 0) {
              loginOK();
              setPublicite("JEMEPPE");
              strcpy(messageEnvoye,"");
              sprintf(messageEnvoye, "CONSULT#1");
              Echange(messageEnvoye, messageRecu);
              ARTICLE a;
              a = remplirArticle(messageRecu);
              setArticle(a.intitule,a.prix,a.stock,a.image);

              

              dialogueMessage("Authentification réussie", "Vous êtes connecté !");
        }
    }

}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonLogout_clicked()
{
  char messageRecu[1400];
  char messageEnvoye[1400];
    
  strcpy(messageEnvoye, "");
  strcpy(messageEnvoye, "LOGOUT#oui");
  Echange(messageEnvoye, messageRecu);

  logoutOK();
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonSuivant_clicked()
{
  char messageRecu[1400];
  char messageEnvoye[1400];
  
  if(numArticle == 21)
    dialogueErreur("Numero d'article", "Plus d'articles...");
  else{
    numArticle++;
    sprintf(messageEnvoye, "CONSULT#%d",numArticle);
    Echange(messageEnvoye, messageRecu);
    ARTICLE a;
    a = remplirArticle(messageRecu);
    setArticle(a.intitule,a.prix,a.stock,a.image);
  }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonPrecedent_clicked()
{
  char messageRecu[1400];
  char messageEnvoye[1400];
  if(numArticle == 1)
    dialogueErreur("Numero d'article", "Plus d'articles...");
  else{
    numArticle--;
    sprintf(messageEnvoye, "CONSULT#%d",numArticle);
    Echange(messageEnvoye, messageRecu);
    ARTICLE a;
    a = remplirArticle(messageRecu);
    setArticle(a.intitule,a.prix,a.stock,a.image);
  }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonAcheter_clicked()
{
  char messageRecu[1400];
  char messageEnvoye[1400];
  char *tmp;
  float prix;
  bool ok;
  int j;
  
  sprintf(messageEnvoye, "ACHAT#%d#%d",numArticle,getQuantite());
  Echange(messageEnvoye, messageRecu);

  tmp = strtok(messageRecu, "#");
  strcpy(tmp,strtok(NULL,"#"));

  if(strcmp(tmp,"ok") == 0 ){

    strcpy(tmp,strtok(NULL,"#"));
    prix = atof(strtok(NULL,"."));
    prix = prix + atof(strtok(NULL,"#"))/1000000;

    for (j = 0 ,ok = true; j< 20 && ok == true; j++)
    {
      if(tabPanierClient[j].id == 0 || tabPanierClient[j].id == numArticle)
      {
        tabPanierClient[j].id = numArticle;
        strcpy(tabPanierClient[j].intitule,  tmp  );
        tabPanierClient[j].prix = prix;
        tabPanierClient[j].quantite = tabPanierClient[j].quantite + getQuantite();
        printf("id = %d  -  prix = %f - qt = %d\n",tabPanierClient[j].id,tabPanierClient[j].prix,tabPanierClient[j].quantite);     
        ok = false;
      }
    }
    majCaddie();
    sprintf(messageEnvoye, "CONSULT#%d",numArticle);
    Echange(messageEnvoye, messageRecu);
    ARTICLE a;
    a = remplirArticle(messageRecu);
    setArticle(a.intitule,a.prix,a.stock,a.image);

  }

}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonSupprimer_clicked()
{
  char messageRecu[1400];
  char messageEnvoye[1400];
  char tmp[50];

  if(getIndiceArticleSelectionne() == -1) dialogueErreur("Erreur selection", "Aucun article selectionne");
  else{
    sprintf(messageEnvoye, "CANCEL#%d",tabPanierClient[getIndiceArticleSelectionne()].id);
    Echange(messageEnvoye, messageRecu);
    strcpy(tmp,strtok(messageRecu,"#"));
    strcpy(tmp,strtok(NULL,"#"));
    if(strcmp(tmp,"ok") == 0 )
    {
      if(numArticle == tabPanierClient[getIndiceArticleSelectionne()].id)
      {
        //requete pour cote client
        sprintf(messageEnvoye, "CONSULT#%d",numArticle);
        Echange(messageEnvoye, messageRecu);
        ARTICLE a;
        a = remplirArticle(messageRecu);
        setArticle(a.intitule,a.prix,a.stock,a.image);
      }

      tabPanierClient[getIndiceArticleSelectionne()].id = 0;
      tabPanierClient[getIndiceArticleSelectionne()].prix = 0;
      tabPanierClient[getIndiceArticleSelectionne()].quantite = 0;

      majCaddie();

    }
  }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonViderPanier_clicked()
{

}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonPayer_clicked()
{

}

//***** Fin de connexion ********************************************
void HandlerSIGINT(int s)
{
    char messageRecu[1400];
    char messageEnvoye[1400];
    printf("\nArret du client.\n");
    strcpy(messageEnvoye, "");
    strcpy(messageEnvoye, "LOGOUT#oui");
    Echange(messageEnvoye, messageRecu);

    shutdown(sClient,SHUT_RDWR);
    exit(0);
}
//***** Echange de données entre client et serveur ******************
void Echange(char* requete, char* reponse)
{
    int nbEcrits, nbLus;
    // ***** Envoi de la requete ****************************
    if ((nbEcrits = Send(sClient,requete,strlen(requete))) == -1)
    {
        perror("Erreur de Send");
        close(sClient);
        exit(1);
    }
    // ***** Attente de la reponse **************************
    if ((nbLus = Receive(sClient,reponse)) < 0)
    {
        perror("Erreur de Receive");
        close(sClient);
        exit(1);
    }
    if (nbLus == 0)
    {
        printf("Serveur arrete, pas de reponse reçue...\n");
        close(sClient);
        exit(1);
    }
    reponse[nbLus] = 0;
}
//MES FONCTIONS
ARTICLE WindowClient::remplirArticle(char* m)
{
    ARTICLE a;
    char* token = strtok(m, "#");
    token = strtok(NULL, "#");
    token = strtok(nullptr, "#");
    a.id = atoi(token);
    token = strtok(nullptr, "#");
    strncpy(a.intitule, token, sizeof(a.intitule) - 1);
    a.intitule[sizeof(a.intitule) - 1] = '\0';
    token = strtok(nullptr, "#");
    a.prix = atof(token);
    token = strtok(nullptr, "#");
    a.stock = atoi(token);
    token = strtok(nullptr, "#");
    strncpy(a.image, token, sizeof(a.image) - 1);
    a.image[sizeof(a.image) - 1] = '\0';

    return a;

}

void WindowClient::majCaddie()
{
  float total = 0;
  videTablePanier();
  for (int j = 0 ; j<20;j++)
  {
    if(tabPanierClient[j].id !=0)
    {
      ajouteArticleTablePanier(tabPanierClient[j].intitule, tabPanierClient[j].prix, tabPanierClient[j].quantite);
      total = total + tabPanierClient[j].prix*tabPanierClient[j].quantite;
    }

  }
  setTotal(total);
}