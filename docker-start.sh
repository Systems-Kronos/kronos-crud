#!/bin/bash
set -e

CATALINA_HOME=/usr/local/tomcat
WAR_TMP=/tmp/KronosCRUD_war_exploded.war
CTX_DIR=${CATALINA_HOME}/webapps/KronosCRUD_war_exploded

# limpa qualquer coisa anterior
rm -rf ${CATALINA_HOME}/webapps/*
rm -rf ${CATALINA_HOME}/work/*
rm -rf ${CATALINA_HOME}/temp/*

# cria pasta de contexto e extrai o WAR
mkdir -p "${CTX_DIR}"
unzip -q "${WAR_TMP}" -d "${CTX_DIR}"

# --- GAMBIARRA: criar symlinks lowercase para diretórios com maiúsculas em WEB-INF/classes
CLASSES_DIR="${CTX_DIR}/WEB-INF/classes"

if [ -d "${CLASSES_DIR}" ]; then
  # percorre todos os diretórios e cria symlink lowercase quando necessário
  find "${CLASSES_DIR}" -type d | while IFS= read -r d; do
    # ignora o próprio classes root
    if [ "${d}" = "${CLASSES_DIR}" ]; then
      continue
    fi

    lc=$(echo "${d}" | tr '[:upper:]' '[:lower:]')
    if [ "${lc}" != "${d}" ] && [ ! -e "${lc}" ]; then
      # cria diretório pai do link se necessário
      parent=$(dirname "${lc}")
      mkdir -p "${parent}"
      ln -s "$(realpath --relative-to="${parent}" "${d}")" "${lc}"
      echo "Created symlink: ${lc} -> ${d}"
    fi
  done
fi

# Também criar symlinks para diretórios de recursos estáticos se necessário
# (ex.: /WEB-INF/pages etc) — cria lowercase para todo webapp
WEBAPP_ROOT="${CTX_DIR}"
find "${WEBAPP_ROOT}" -type d | while IFS= read -r d; do
  lc=$(echo "${d}" | tr '[:upper:]' '[:lower:]')
  if [ "${lc}" != "${d}" ] && [ ! -e "${lc}" ]; then
    parent=$(dirname "${lc}")
    mkdir -p "${parent}"
    ln -s "$(realpath --relative-to="${parent}" "${d}")" "${lc}"
    echo "Created symlink: ${lc} -> ${d}"
  fi
done

# Por segurança, exibir estrutura relevante
echo "=== Estrutura WEB-INF/classes (list) ==="
ls -la "${CLASSES_DIR}" || true
echo "=== End ==="

# Inicia o Tomcat (modo foreground)
exec catalina.sh run
