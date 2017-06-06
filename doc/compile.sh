#!/usr/bin/env bash
set -ev

pandoc ru.md --latex-engine=xelatex -o ru.pdf
pandoc en.md --latex-engine=xelatex -o en.pdf
