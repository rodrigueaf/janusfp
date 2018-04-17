#language: fr
#Auteur: AFFODOGANDJI Claude Rodrigue

Fonctionnalité: Gestion des profils

  Le profil représente un groupe d'utilisateur ayant les mêmes permissions.
  Le profil contient les informations suivantes:
  - Le nom
  - La liste des permissions

  Scénario: Ajouter un profil avec succès
    Etant donné qu'aucun profil n'était enregistré
    Et qu'on dispose d'un profil valide à enregistrer
    Lorsqu' on ajoute le profil
    Alors on obtient une réponse confirmant la réussite de l'opération d'ajout du profil
    Et 1 profil est créé
    Et le profil créé est celui soumis à l'ajout

  Scénario: Ajouter un profil dont le nom existe déjà
    Etant donné qu'un profil était déjà enregistré
    Et qu'on dispose d'un profil avec le même nom à enregistrer
    Lorsqu' on ajoute le profil
    Alors on obtient le message sur le profil "Un profil avec le même nom existe déjà"
    Et 1 profil demeure créé

  Scénario: Ajouter un profil dont le nom n'est pas renseigné
    Etant donné qu'aucun profil n'était enregistré
    Et qu'on dispose d'un profil dont le nom n'est pas renseigné
    Lorsqu' on ajoute le profil
    Alors on obtient le message sur le profil "Le nom est obligatoire"
    Et 0 profil demeure créé

  Scénario: Modifier un profil avec succès
    Etant donné qu'un profil était déjà enregistré
    Et qu'on récupère ce profil pour le modifier
    Lorsqu' on modifie le profil
    Alors on obtient une réponse confirmant la réussite de l'opération de modification du profil

  Scénario: Modifier un profil en ne renseignant pas le nom
    Etant donné qu'un profil était déjà enregistré
    Et qu'on récupère ce profil pour le modifier en ne renseignant pas le nom
    Lorsqu' on modifie le profil
    Alors on obtient le message sur le profil "Le nom est obligatoire"

  Scénario: Modifier un profil en renseignant un nom déjà existant
    Etant donné que deux profils étaient déjà enregistrés
    Et qu'on récupère ce profil pour modifier son nom par une valeur qui existe déjà
    Lorsqu' on modifie le profil
    Alors on obtient le message sur le profil "Un profil avec le même nom existe déjà"

  Scénario: Consulter les détails d'un profil
    Etant donné qu'un profil était déjà enregistré
    Lorsqu' on récupère ce profil
    Alors on obtient le profil déjà enregistré

  Scénario: Récupérer la liste des profils
    Etant donné qu'un profil était déjà enregistré
    Lorsqu' on récupère la liste des profils
    Alors on a une liste contenant le profil déjà enregistré

  Scénario: Supprimer un profil avec succès
    Etant donné qu'un profil était déjà enregistré
    Lorsqu' on supprime le profil
    Alors on obtient une réponse confirmant la réussite de l'opération de suppression du profil
    Et 0 profil est créé

  Scénario: Supprimer un profil déjà utilisé
    Etant donné qu'un profil était déjà enregistré
    Et que ce profil est déjà attribué à un utilisateur
    Lorsqu' on supprime le profil
    Alors on obtient le message sur le profil "Ce profil a déjà été attribué à un utilisateur. Il ne peut plus être supprimé"
    Et 1 profil demeure créé

  Scénario: Rechercher un profil
    Etant donné que deux profils étaient déjà enregistrés
    Lorsqu' on recherche des profils en fonction d'un critère
    Alors les profils récupérés doivent respecter le critère défini

  Scénario: Activer un profil
    Etant donné qu'un profil était déjà enregistré
    Lorsqu' on active le profil
    Alors le profil récupéré doit être activé

  Scénario: Désactiver un profil
    Etant donné qu'un profil était déjà enregistré
    Lorsqu' on désactive le profil
    Alors le profil récupéré doit être désactivé
