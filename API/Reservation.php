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

if (isset($_POST['id_user']) && isset($_POST['id_place']) && isset($_POST["heure_debut"])) {
    
    $id_user = $_POST["id_user"];
    $id_place = $_POST["id_place"];
    $id_parking = $_POST["id_parking"];
    $heure_debut = $_POST["heure_debut"];
    $heure_fin = $_POST["heure_fin"];
        
    $bdd->query("INSERT INTO reservation VALUES (NULL,'$id_user','$id_place','$id_parking','$heure_debut','$heure_fin')");
    
    $bdd->query("UPDATE parkings SET place_dispo = place_dispo + 1 WHERE id='$id_parking'");

    $response = array(); 
    $response["success"] = true;
    echo json_encode($response);

}
elseif (isset($_POST['id_user']) && isset($_POST['id_place'])) {
    
    $id_user = $_POST["id_user"];
    $id_place = $_POST["id_place"];
    $id_parking = $_POST["id_parking"];
    
    // On met à l'état occupé la place demandée
    $bdd->query("UPDATE places SET etat = 1 WHERE id='$id_place'");

    //On décrémente le nombre de places dispo dans le parking
    $bdd->query("UPDATE parkings SET place_dispo = place_dispo - 1 WHERE id='$id_parking'");

    $response = array(); 
    $response["success"] = true;
    echo json_encode($response);

}
elseif (isset($_POST['id_user'])) {

    $iduser = $_POST['id_user'];
      
    $count = $bdd->query("SELECT count(*) as nombre from reservation");
    $nbr = $count->fetch();
    if($nbr['nombre'] != 0){
        $data = array();
        $response = array();
        $query = $bdd->query("SELECT * from reservation where id_user = '$iduser'");
        while ($donnees = $query->fetch()){ 
                $data["id"] = $donnees["id"];
                $data["id_user"] = $donnees["id_user"];
                $data["id_place"] = $donnees["id_place"];
                $data["id_parking"] = $donnees["id_parking"];
                $data["heure_debut"] = $donnees["heure_debut"];
                $data["heure_fin"] = $donnees["heure_fin"];
                array_push($response, $data);
        }

    $query->closeCursor();

    echo "{\"reservations\": [".ltrim(json_encode($response), '[')."}";
    }else{

        $response = array(); 
        $response["success"] = false;
        echo json_encode($response);
    }
}else{
        $response = array(); 
        $response["success"] = false;
        echo json_encode($response);
}
?>

