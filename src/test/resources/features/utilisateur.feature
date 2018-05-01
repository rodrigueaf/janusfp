#language: fr
#Auteur: AFFODOGANDJI Claude Rodrigue

Fonctionnalité: Gestion des utilisateurs

  Un utilisateur représente une entité personne physique qui effectue des actions dans le système.
  Un utilisateur contient les informations suivantes:
  - Le nom
  - Le prénom
  - Le login
  - Le mot de passe
  - La liste de ses permissions

  Scénario: Ajouter un utilisateur avec succès
    Etant donné qu'aucun utilisateur n'était enregistré
    Et qu'on dispose d'un utilisateur valide à enregistrer
    Lorsqu' on ajoute l'utilisateur
    Alors on obtient une réponse confirmant la réussite de l'opération d'ajout de l'utilisateur
    Et 1 utilisateur est créé
    Et l’utilisateur créé est celui soumis à l'ajout

  Scénario: Ajouter un utilisateur dont le login existe déjà
    Etant donné qu'un utilisateur était déjà enregistré
    Et qu'on dispose d'un utilisateur avec le même login à enregistrer
    Lorsqu' on ajoute l'utilisateur
    Alors on obtient le message sur l'utilisateur "Un utilisateur avec le même login existe déjà"
    Et 1 utilisateur demeure créé

  Scénario: Ajouter un utilisateur dont le login n'est pas renseigné
    Etant donné qu'aucun utilisateur n'était enregistré
    Et qu'on dispose d'un utilisateur dont le login n'est pas renseigné
    Lorsqu' on ajoute l'utilisateur
    Alors on obtient le message sur l'utilisateur "Le login est obligatoire"
    Et 0 utilisateur demeure créé

  Scénario: Modifier un utilisateur avec succès
    Etant donné qu'un utilisateur était déjà enregistré
    Et qu'on récupère cet utilisateur pour le modifier
    Lorsqu' on modifie l'utilisateur
    Alors on obtient une réponse confirmant la réussite de l'opération de modification de l'utilisateur

  Scénario: Modifier un utilisateur en ne renseignant pas le nom
    Etant donné qu'un utilisateur était déjà enregistré
    Et qu'on récupère cet utilisateur pour le modifier en ne renseignant pas le nom
    Lorsqu' on modifie l'utilisateur
    Alors on obtient le message sur l'utilisateur "Le nom est obligatoire"

  Scénario: Modifier un utilisateur en ne renseignant pas le login
    Etant donné qu'un utilisateur était déjà enregistré
    Et qu'on récupère cet utilisateur pour le modifier en ne renseignant pas le login
    Lorsqu' on modifie l'utilisateur
    Alors on obtient le message sur l'utilisateur "Le login est obligatoire"

  Scénario: Modifier un utilisateur en renseignant un login déjà existant
    Etant donné que deux utilisateurs étaient déjà enregistrés
    Et qu'on récupère cet utilisateur pour modifier son login par une valeur qui existe déjà
    Lorsqu' on modifie l'utilisateur
    Alors on obtient le message sur l'utilisateur "Un utilisateur avec le même login existe déjà"

  Scénario: Consulter les détails d'un utilisateur
    Etant donné qu'un utilisateur était déjà enregistré
    Lorsqu' on récupère cet utilisateur
    Alors on obtient l'utilisateur déjà enregistré

  Scénario: Récupérer la liste des utilisateurs
    Etant donné qu'un utilisateur était déjà enregistré
    Lorsqu' on récupère la liste des utilisateurs
    Alors on a une liste contenant l'utilisateur déjà enregistré

  Scénario: Supprimer un utilisateur avec succès
    Etant donné qu'un utilisateur était déjà enregistré
    Lorsqu' on supprime l'utilisateur
    Alors on obtient une réponse confirmant la réussite de l'opération de suppression de l'utilisateur
    Et 0 utilisateur est créé

  Scénario: Supprimer un utilisateur déjà utilisé
    Etant donné qu'un utilisateur était déjà enregistré
    Et que ce utilisateur a déjà effectué une opération
    Lorsqu' on supprime l'utilisateur
    Alors on obtient le message sur l'utilisateur "Cet utilisateur a déjà effectué une opération. Il ne peux plus être supprimé"
    Et 1 utilisateur demeure créé

  Scénario: Rechercher un utilisateur
    Etant donné que deux utilisateurs étaient déjà enregistrés
    Lorsqu' on recherche des utilisateurs en fonction d'un critère
    Alors les utilisateurs récupérés doivent respecter le critère défini

  Scénario: Activer un utilisateur
    Etant donné qu'un utilisateur était déjà enregistré
    Lorsqu' on active l'utilisateur
    Alors l’utilisateur récupéré doit être activé

  Scénario: Désactiver un utilisateur
    Etant donné qu'un utilisateur était déjà enregistré
    Lorsqu' on désactive l'utilisateur
    Alors l’utilisateur récupéré doit être désactivé

  Scénario: Attribuer des droits à un utilisateur
    Etant donné qu'un utilisateur était déjà enregistré
    Lorsqu' on attribue un profil à l'utilisateur
    Alors l'utilisateur dispose des droits de ce profil