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

if (isset($_POST['id_parking']) && isset($_POST['heure_debut']) && isset($_POST['heure_fin']) ) {
    
    $idpark = $_POST['id_parking'];
    $heure_debut = $_POST['heure_debut'];
    $heure_fin = $_POST['heure_fin'];
        
    $count = $bdd->query("SELECT count(*) as nombre from places");
    $nbr = $count->fetch();
    if($nbr['nombre'] != 0){
        $data = array();
        $response = array();
        $query = $bdd->query("SELECT * FROM places
            WHERE id_parking = '$idpark' 
            AND id NOT IN
            (SELECT id_place FROM `reservation` WHERE '$heure_debut' BETWEEN `heure_debut` AND `heure_fin` OR '$heure_fin' BETWEEN `heure_debut` AND `heure_fin`)
        ");
        while ($donnees = $query->fetch()){ 
            if($donnees["etat"] == "0"){
                $data["id"] = $donnees["id"];
                $data["nom"] = $donnees["nom"];
                $data["id_parking"] = $donnees["id_parking"];
                $data["tarif"] = $donnees["tarif"];
                $data["etat"] = $donnees["etat"];
                $data["etage"] = $donnees["etage"];
                array_push($response, $data);
            }
        }

    $query->closeCursor();

    echo "{\"places\": [".ltrim(json_encode($response), '[')."}";
    }else{

        $response = array(); 
        $response["success"] = false;
        echo json_encode($response);
    }
}
elseif (isset($_POST['id_parking'])) {

    $idpark = $_POST['id_parking'];
      
    $count = $bdd->query("SELECT count(*) as nombre from places");
    $nbr = $count->fetch();
    if($nbr['nombre'] != 0){
        $data = array();
        $response = array();
        $query = $bdd->query("SELECT * from places where id_parking = '$idpark'");
        while ($donnees = $query->fetch()){ 
            if($donnees["etat"] == "0"){
                $data["id"] = $donnees["id"];
                $data["nom"] = $donnees["nom"];
                $data["id_parking"] = $donnees["id_parking"];
                $data["tarif"] = $donnees["tarif"];
                $data["etat"] = $donnees["etat"];
                $data["etage"] = $donnees["etage"];
                array_push($response, $data);
            }
        }

    $query->closeCursor();

    echo "{\"places\": [".ltrim(json_encode($response), '[')."}";
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

