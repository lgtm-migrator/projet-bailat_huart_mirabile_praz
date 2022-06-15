# statique - Générateur de site web statique en CLI

Réalisé dans le cadre du projet de semestre du cours DIL à la HEIG-VD

[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/dil-classroom/projet-bailat_huart_mirabile_praz.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/dil-classroom/projet-bailat_huart_mirabile_praz/context:java)- [statique - Générateur de site web statique en CLI](#statique---générateur-de-site-web-statique-en-cli)

## Table des matières

- [Prérequis d'utilisation](#prérequis-dutilisation)
- [Téléchargement et utiisation](#téléchargement-et-utiisation)
  - [Linux et MacOS](#linux-et-macos)
  - [Windows](#windows)
- [Commandes disponibles](#commandes-disponibles)
  - [`init` - Création d'un nouveau site statique](#init---création-dun-nouveau-site-statique)
  - [`build` - Construction du site](#build---construction-du-site)
  - [`clean` - Supprimer les fichiers HTML du site](#clean---supprimer-les-fichiers-html-du-site)
  - [`serve` - Publier le site à l'aide du serveur Web intégré](#serve---publier-le-site-à-laide-du-serveur-web-intégré)
  - [`publish` - Publier le site sur un serveur SFTP distant](#publish---publier-le-site-sur-un-serveur-sftp-distant)
  - [`version` - Afficher la version actuelle du générateur](#version---afficher-la-version-actuelle-du-générateur)
- [Templating](#templating)
- [Configuration du site](#configuration-du-site)

## Prérequis d'utilisation

- Le générateur nécessite d'avoir Java d'installé et accessible globalement via la ligne de commande (Java doit avoir été ajouté au PATH).
- Le générateur a été développé et testé sous Java 11. L'utilisation d'une version supérieure n'a pas été testée, donc elle se fait à vos risques et périls.

## Téléchargement et utiisation

- Le générateur ne nécessite pas d'installation, car il se limite à un fichier exécutable Java. Cependant, vous pouvez l'ajouter à votre `PATH` si vous le souhaitez, afin de pouvoir l'utiliser de manière globale.

### Linux et MacOS

1. Téléchargez la [dernière version](https://github.com/dil-classroom/projet-bailat_huart_mirabile_praz/releases) du générateur et décompressez l'archive ZIP.
2. Dans un terminal, rendez-vous dans le répertoire décompressé.
3. Vous pouvez utiliser le générateur à l'aide de la commande `./statique <option>`.

### Windows

1. Téléchargez la [dernière version](https://github.com/dil-classroom/projet-bailat_huart_mirabile_praz/releases) du générateur et décompressez l'archive ZIP.
2. Dans l'invite de commandes, rendez-vous dans le répertoire décompressé.
3. Vous pouvez utiliser le générateur à l'aide de la commande `java -jar statique-<version>.jar <option>` (en remplaçant `<version>` par le numéro de version téléchargée.)

## Commandes disponibles

Voici la liste des commandes disponibles.

### `init` - Création d'un nouveau site statique

```text
statique init <chemin>
```

- Remplacer `<chemin>` par le chemin relatif ou absolu vers un répertoire. Le répertoire **ne doit pas déjà exister**.
- Après exécution, le répertoire spécifié est créé. Il contient deux fichiers :
  - `config.yml`, qui contient les informations de configuration du site. Se référer à la section [Configuration du site](#configuration-du-site) pour plus d'informations.
  - `index.md`, qui constitue la page d'accueil du site. Vous êtes libres de dupliquer ce fichier sous un autre nom pour créer d'autres pages.

### `build` - Construction du site

```text
statique build <chemin> [--watch]
```

- Remplacer `<chemin>` par le chemin relatif ou absolu vers le répertoire d'un site statique existant. Le répertoire **doit exister**.
- Les fichiers HTML résultants sont créés dans un sous-répertoire `build/`. Ce sont ces fichiers qui doivent être copiés sur votre serveur Web pour publication.
- Le paramètre `--watch` optionnel permet de demander au générateur de construire le site statique automatiquement à chaque modification d'un fichier source Markdown.
  - Pour stopper ce fonctionnement, il faut arrêter l'exécution de la commande à l'aide du raccourci clavier `Ctrl+C` dans le terminal.

### `clean` - Supprimer les fichiers HTML du site

```text
statique clean <chemin>
```

- Remplacer `<chemin>` par le chemin relatif ou absolu vers le répertoire d'un site statique existant. Le répertoire **doit exister**.
- **Attention !** Cette commande a pour effet d'écraser le répertoire `build/`. Donc toute donnée non sauvegardée sera perdue !

### `serve` - Publier le site à l'aide du serveur Web intégré

```text
statique serve <chemin>
```

- Remplacer `<chemin>` par le chemin relatif ou absolu vers le répertoire d'un site statique existant. Le répertoire **doit exister**.
- Cette commande va implicitement lancer la construction du site (commande `build`). Le contenu éventuel d'un précédent *build* sera donc écrasé.
- Le site web sera accessible sur le port TCP `8080` de la machine courante. Ce port doit donc être disponible. Il ne peut malheureusement pas être modifié.
- Le paramètre `--watch` optionnel permet de demander au générateur de servrir le site statique automatiquement à chaque modification d'un fichier source Markdown.
  - Pour stopper ce fonctionnement, il faut arrêter l'exécution de la commande à l'aide du raccourci clavier `Ctrl+C` dans le terminal.

### `publish` - Publier le site sur un serveur SFTP distant

```text
statique publish <chemin>
```

- Remplacer `<chemin>` par le chemin relatif ou absolu vers le répertoire d'un site statique existant. Le répertoire **doit exister**.
- Cette commande va implicitement lancer la construction du site (commande `build`). Le contenu éventuel d'un précédent *build* sera donc écrasé.
- Les informations de connexion au serveur SFTP doivent figurer dans le fichier de configuration du site (`config.yaml`). Se référer à la section [Configuration du site](#configuration-du-site) pour plus d'informations.

### `version` - Afficher la version actuelle du générateur

```text
statique -version
```

- Le générateur ne dispose malheureusement pas de système de mise à jour automatique. Cette commande vous permet donc de voir quelle version est actuellement utilisée afin de mettre à jour le générateur si une nouvelle version est disponible.

## Templating

- Le templating permet d'insérer du contenu dans les pages en se basant sur les métadonnées du site et des pages. Le moteur de template utilisé est [Handlebars](https://github.com/jknack/handlebars.java).
- La configuration minimale pour le templating est de créer un dossier `template` dans le dossier du site et y créer un fichier `layout.html`.
- Les variables mise à disposition sont:
  - `site.title` titre du site
  - `site.site_desc` description du site
  - `site.domain` domaine du site
  - `page.author` auteur de le page
  - `page.title` titre de la page
  - `page.date` date de création
  - `content` contenu de la page
- Il est aussi possible d'inclure des autres templates via la directive `include`.
- Example de template

```html
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>{{ site.title }} | {{ page.title }}</title>
</head>
<body>
{{ include "menu.html" }}
{{ content }}
</body>
</html>
```

## Configuration du site

- Lorsqu'un site est nouvellement généré, le fichier de configuration `config.yaml` contient les champs suivants :

```yaml
title: ''         # Titre du site
siteDesc: ''      # Description du site
domain: ''        # Nom de domaine public du site (par ex. monsite.com)
ssh_distpath: ''  # Chemin distant où publier le site sur le serveur SFTP
ssh_hostname: ''  # Nom d'hôte ou adresse IP du serveur SFP
ssh_username: ''  # Nom d'utilisateur du serveur SFTP
ssh_password: ''  # Mot de passe du compte SFTP (oui...) 
```

- Les champs `title` et `siteDesc` sont utilisables comme variables dans les templates du site. Se référer à la section [Templating](#templating) pour plus d'informations.

- Les champs commençant par `ssh_` concernent les informations de connexion au serveur SFTP distant.
