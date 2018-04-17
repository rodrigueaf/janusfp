# language: fr
# auteur Claude Rodrigue AFFODOGANDJI

Fonctionnalité: Gestion des budgets

  Les informations du budget sont :
  - La date début
  - La date fin
  - Le libellé
  - Le montant du budget

  Scénario: Ajouter un budget avec succès
    Etant donné qu'aucun budget n'était enregistré
    Et qu'on dispose d'un budget valide à enregistrer
    Lorsqu' on ajoute le budget
    Alors on obtient une réponse confirmant la réussite de l'opération d'ajout du budget
    Et 1 budget est créé
    Et le budget créé est celui soumis à l'ajout

  Scénario: Ajouter un budget dont le libellé existe déjà
    Etant donné qu'un budget était déjà enregistré
    Et qu'on dispose d'un budget avec le même libellé à enregistrer
    Lorsqu' on ajoute le budget
    Alors on obtient le message sur le budget "Un budget avec le même libellé existe déjà"
    Et 1 budget demeure créé

  Scénario: Ajouter un budget dont le libellé n'est pas renseigné
    Etant donné qu'aucun budget n'était enregistré
    Et qu'on dispose d'un budget dont le libellé n'est pas renseigné
    Lorsqu' on ajoute le budget
    Alors on obtient le message sur le budget "Le libellé est obligatoire"
    Et 0 budget demeure créé

  Scénario: Modifier un budget avec succès
    Etant donné qu'un budget était déjà enregistré
    Et qu'on récupère ce budget pour le modifier
    Lorsqu' on modifie le budget
    Alors on obtient une réponse confirmant la réussite de l'opération de modification du budget

  Scénario: Modifier un budget en ne renseignant pas le libellé
    Etant donné qu'un budget était déjà enregistré
    Et qu'on récupère ce budget pour le modifier en ne renseignant pas le libellé
    Lorsqu' on modifie le budget
    Alors on obtient le message sur le budget "Le libellé est obligatoire"

  Scénario: Modifier un budget en renseignant un libellé déjà existant
    Etant donné que deux budgets étaient déjà enregistrés
    Et qu'on récupère ce budget pour modifier son libellé par une valeur qui existe déjà
    Lorsqu' on modifie le budget
    Alors on obtient le message sur le budget "Un budget avec le même libellé existe déjà"

  Scénario: Consulter les détails d'un budget
    Etant donné qu'un budget était déjà enregistré
    Lorsqu' on récupère ce budget
    Alors on obtient le budget déjà enregistré

  Scénario: Récupérer la liste des budgets
    Etant donné qu'un budget était déjà enregistré
    Lorsqu' on récupère la liste des budgets
    Alors on a une liste contenant le budget déjà enregistré

  Scénario: Supprimer un budget avec succès
    Etant donné qu'un budget était déjà enregistré
    Lorsqu' on supprime le budget
    Alors on obtient une réponse confirmant la réussite de l'opération de suppression du budget
    Et 0 budget est créé

  Scénario: Rechercher un budget
    Etant donné que deux budgets étaient déjà enregistrés
    Lorsqu' on recherche des budgets en fonction d'un critère
    Alors les budgets récupérés doivent respecter le critère défini