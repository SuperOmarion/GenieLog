<?php
        try
        {
            $bdd = new PDO('mysql:host=xxxxxxxxx;dbname=xxxxxxx;charset=utf8','xxxxxx','Oxxxxxxx');
        }
        catch(Exception $e)
        {
                die('Erreur : '.$e->getMessage());
        }

        if (isset($_POST['name']) && isset($_POST['mot_de_passe'])) {
            $nom = $_POST['name'];
            $pass = $_POST['mot_de_passe'];
            $count = $bdd->query("SELECT count(*) as nombre from users where nom='$nom' and mot_de_passe = '$pass' ");
            $nbr = $count->fetch();
            if($nbr['nombre'] == 1){
                $query = $bdd->query("SELECT role from users where nom='$nom'");
                $data = $query->fetch();
                //le compte existe pas de coup on le connecte :p
                $response = array();
                $response["success"] = true;
                $response["role"] = $data['role'];  
                echo json_encode($response);

            }else if($nbr['nombre'] == 0){
                $response = array();
                $response["success"] = false;
                echo json_encode($response); 
            }
        }else{
            //le compte n'existe pas il faut en creer un 
            $response = array();
            $response["success"] = false;
            echo json_encode($response); 
        }
        //header('Location: index.php');
?>

