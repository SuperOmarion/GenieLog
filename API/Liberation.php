<?php
// Connexion à la base de données
try
{
    $bdd = new PDO('mysql:host=db725926453.db.1and1.com;dbname=db725926453;charset=utf8','dbo725926453','OMARupmc@26');
}
catch(Exception $e)
{
        die('Erreur : '.$e->getMessage());
}

if (isset($_POST['id_user']) && isset($_POST['id_place'])) {
    
    $id_user = $_POST["id_user"];
    $id_place = $_POST["id_place"];
    $id_parking = $_POST["id_parking"];
    
    // On met à l'état occupé la place demandée
    $bdd->query("UPDATE places SET etat = 0 WHERE id='$id_place'");

    //On décrémente le nombre de places dispo dans le parking
    $bdd->query("UPDATE parkings SET place_dispo = place_dispo + 1 WHERE id='$id_parking'");

    $response = array(); 
    $response["success"] = true;
    echo json_encode($response);
}