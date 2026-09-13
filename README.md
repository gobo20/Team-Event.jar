# EventTeamPlugin - Paper 1.21.8 Build #112

## Was macht das Plugin?
- `/event <Spieler>` (nur OP) -> Spieler bekommt Aufforderung Team zu wählen
- Nachricht: "Schreibe /Blau für blaues Team und /rot für rotes Team"
- `/Blau` -> Teleport zu blau + Blaues Leder Kit
- `/Rot` -> Teleport zu rot + Rotes Leder Kit
- `/event set red` / `/event set blue` setzt die Teleport-Punkte

## Kit
- Leder-Rüstung in Teamfarbe (unzerstörbar)
- Eisenschwert mit Unbreaking 255
- Bogen mit Unbreaking 255
- 256 Pfeile

## Installation
1. `mvn clean package`
2. `target/event-team-plugin-1.0.0.jar` in `plugins/` Ordner
3. Server starten (Paper 1.21.8 #112)
4. Als OP ingame:
   - Gehe zu rotem Spawn -> `/event set red`
   - Gehe zu blauem Spawn -> `/event set blue`
   - Dann `/event SpielerName`

## GitHub
git init
git add .
git commit -m "Event Plugin"
git remote add origin https://github.com/DEINUSER/DEINREPO.git
git push -u origin main
