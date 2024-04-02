CREATE TABLE IF NOT EXISTS coordinateur (
    codeCoordinateur VARCHAR(255) NOT NULL,
    nomCoordinateur VARCHAR(255) NOT NULL,
    prenomCoordinateur VARCHAR(255) NOT NULL,
    courrielCoordinateur VARCHAR(255) NOT NULL UNIQUE,
    mdpCoordinateur VARCHAR(255) NOT NULL,
    PRIMARY KEY (codeCoordinateur)
    );

--
-- CREATE TABLE IF NOT EXISTS adresse (
--     idAdresse INT AUTO_INCREMENT NOT NULL,
--     numeroCivique INT NOT NULL,
--     rue VARCHAR(255) NOT NULL,
--     ville VARCHAR(255) NOT NULL,
--     codePostal VARCHAR(255) NOT NULL,
--     pays VARCHAR(255) NOT NULL,
--     PRIMARY KEY (idAdresse)
--     );
--

CREATE TABLE IF NOT EXISTS entreprise (
    codeEntreprise INT AUTO_INCREMENT NOT NULL,
    nomEntreprise VARCHAR(255) NOT NULL,
    adresse VARCHAR(255) NOT NULL,
    PRIMARY KEY (codeEntreprise)
    );

CREATE TABLE IF NOT EXISTS employeur (
    codeUtilisateur VARCHAR(255) NOT NULL,
    nomUtilisateur VARCHAR(255) NOT NULL,
    prénomUtilisateur VARCHAR(255) NOT NULL,
    courrielUtilisateur VARCHAR(255) NOT NULL,
    téléphoneUtilisateur VARCHAR(255) NOT NULL,
    entreprise_codeEntreprise INT,
    PRIMARY KEY (codeUtilisateur),
    FOREIGN KEY (entreprise_codeEntreprise) REFERENCES entreprise(codeEntreprise)
    );

CREATE TABLE IF NOT EXISTS offre (
    idOffre INT AUTO_INCREMENT NOT NULL,
    employeur_codeEmployeur VARCHAR(255) NOT NULL,
    titrePoste VARCHAR(255) NOT NULL,
    modeEmploi ENUM('PRÉSENTIEL', 'ÀDISTANCE', 'HYBRIDE') NOT NULL,
    description TEXT,
    PRIMARY KEY (idOffre),
    FOREIGN KEY (employeur_codeEmployeur) REFERENCES employeur(codeUtilisateur)
    );

CREATE TABLE IF NOT EXISTS etudiant (
    codeEtudiant VARCHAR(255) NOT NULL PRIMARY KEY,
    nomEtudiant VARCHAR(255) NOT NULL,
    prenomEtudiant VARCHAR(255) NOT NULL,
    courrielEtudiant VARCHAR(255) NOT NULL UNIQUE,
    telephoneEtudiant VARCHAR(255) NOT NULL,
    profilInformatique ENUM('PROGRAMMATION', 'RÉSAUTIQUE') NOT NULL,
    stageIntegration BOOLEAN NOT NULL,
    adresseEtudiant VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS candidature (
    codeCandidature INT AUTO_INCREMENT NOT NULL,
    offre_idOffre INT,
    etudiant_codeEtudiant VARCHAR(255) NOT NULL,
    étatCandidature VARCHAR(255) NOT NULL,
    PRIMARY KEY (codeCandidature),
    FOREIGN KEY (offre_idOffre) REFERENCES offre(idOffre),
    FOREIGN KEY (etudiant_codeEtudiant) REFERENCES etudiant(codeEtudiant)
    );

CREATE TABLE IF NOT EXISTS Stage (
    idStage INT AUTO_INCREMENT NOT NULL,
    stagiaire_codeEtudiant VARCHAR(255) NOT NULL,
    candidature_codeCandidature INT NOT NULL,
    superviseur_assigne VARCHAR(255) NOT NULL,
    employeur_codeEmployeur VARCHAR(255) NOT NULL,
    stage_progression ENUM('DÉBUTÉ', 'ENCOURS', 'TERMINÉ'),
    lieu VARCHAR(255) NOT NULL,
    date_debut VARCHAR(255) NOT NULL,
    date_fin VARCHAR(255) NOT NULL,
    PRIMARY KEY (idStage),
    FOREIGN KEY (stagiaire_codeEtudiant) REFERENCES etudiant(codeEtudiant),
    FOREIGN KEY (candidature_codeCandidature) REFERENCES candidature(codeCandidature),
    FOREIGN KEY (employeur_codeEmployeur) REFERENCES employeur(codeUtilisateur)
    );
