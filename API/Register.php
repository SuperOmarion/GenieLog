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

if (isset($_POST['name']) && isset($_POST['telephone']) && isset($_POST['mot_de_passe'])) {

    $nom = $_POST['name'];
    $tel = $_POST['telephone'];
   //$pass = md5($_POST['mot_de_passe']);
    $pass = $_POST['mot_de_passe'];
  

    $count = $bdd->query("SELECT count(*) as nombre from users where nom='$nom'");
    $nbr = $count->fetch();

    if($nbr['nombre'] == 0){
        //le nom n'existe pas de coup on le cree :p
        $SqlQuerry = $bdd->query("INSERT INTO users (nom,telephone,mot_de_passe) VALUES ('$nom','$tel','$pass')");
        $response = array();
        $response["success"] = true;  
        echo json_encode($response);

    }else{
        //le nom existe il faut choisir un autre
        $response = array();
        $response["success"] = false;  
        echo json_encode($response); 
    }
}else{
    //le nom existe il faut choisir un autre
    $response = array();
    $response["success"] = false;  
    echo json_encode($response); 
}

?>


