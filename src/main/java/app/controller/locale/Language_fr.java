package app.controller.locale;

import java.util.ListResourceBundle;

public class Language_fr extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {

        return resources;
    }

    private final Object[][] resources = {

            { "welcome", "Bienvenue dans notre magasin confortable White Dragon <br> pour continuer, vous devez vous connecter" },
            { "loggin.title", "Entrée du système" },
            { "list.client.productlist", "Ceci est la liste de produits" },
            { "log.in", "S'identifier" },
            { "registration", "Enregistrement" },
            { "user", "Utilisatrice" },
            { "inblacklist", "a été bloqué parce que rendre l'ordre et n'a pas payé" },
            { "loggin", "S'identifier" },
            { "password", "Mot de passe" },
            { "nickname", "Surnom" },
            { "name", "Prénom" },
            { "registration.fail", "Les données entrées sont incorrectes ou l'utilisateur avec ce pseudonyme existe déjà <br> Données correctes - mot de passe de 6 à 15 symboles <br> Nom et pseudo de 3 à 15 symboles "},
            { "nodata", "Les données saisies sont incorrectes" },
            { "blacklist.title", "Liste noire" },
            { "entry", "Vous êtes connecté en tant que" },
            { "admin", "Vous êtes administrateur" },
            { "blacklist.hint", "Vous pouvez débloquer des utilisateurs" },
            { "orders", "Ordres" },
            { "tothestore", "Au magasin" },
            { "out", "En dehors" },
            { "nulldata", "Vous avez choisi rien" },
            { "blacklist.delete", "Supprimer de la liste noire" },
            { "blacklist.empty", "La liste noire est vide" },
            { "admin.list.title", "Liste de produits" },
            { "admin.list.hint", "Vous pouvez ajouter ou supprimer des produits à / de la liste" },
            { "admin.list.no.products", "Aucun produit maintenant" },
            { "admin.list.add.product", "Ajouter un nouveau produit à la liste" },
            { "price", "Prix" },
            { "nomination", "Nomination" },
            { "add", "Ajouter" },
            { "delete", "Effacer" },
            { "exchange.rates", 0.00229 },
            { "currency", "€" },
            { "basket.title", "Votre panier" },
            { "basket.empty", "Votre panier est vide en ce moment" },
            { "basket.get.order", "Obtenir la commande" },
            { "add.to.the.basket", "Ajouter au panier"},
            { "orders.hint", "Vous pouvez supprimer des commandes et bloquer les utilisateurs"},
            { "orders.admin", "Ordres"},
            { "orders.client", "Vos commandes"},
            { "order.admin", "Ordres"},
            { "order.client", "Vos commandes"},
            { "orders.pay", "Payer"},
            { "orders.client.title", "Client - "},
            { "orders.client.blocked", "CE CLIENT A ÉTÉ BLOQUÉ"},
            { "orders.client.block", "Bloquer"},
            { "orders.empty", "Aucune commande"},
            { "orders.order", "De commande №"},
            { "orders.creation", "créé à"},
            { "orders.paid", "Payé"},
            { "orders.notpaid", "Impayé"},
            { "orders.total.price", "Prix total -"},
            { "count", "Count "},
            { "change.language", "changer de langue"},





































    };
}