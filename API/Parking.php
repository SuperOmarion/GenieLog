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
  
$count = $bdd->query("SELECT count(*) as nombre from parkings");
$nbr = $count->fetch();
if($nbr['nombre'] != 0){
	$data = array();
 	$response = array();
	$query = $bdd->query("SELECT * from parkings");
	while ($donnees = $query->fetch()){	
	    $data["id"] = $donnees["id"];
	    $data["nom"] = $donnees["nom"];
	    $data["ref"] = $donnees["ref"];
	    $data["url"] = $donnees["url"];
	    $data["longitude"] = $donnees["longitude"];
	    $data["latitude"] = $donnees["latitude"];
	    $data["nbr_place"] = $donnees["nbr_place"];
	    $data["place_dispo"] = $donnees["place_dispo"];
	    array_push($response, $data);
	}

$query->closeCursor();

echo "{\"parkings\": [".ltrim(json_encode($response), '[')."}";
}else{
    $response = array(); 
    $response["success"] = false;
    echo json_encode($response);
}
?>

