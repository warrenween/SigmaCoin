#!/bin/bash
set -ev

pandoc doc/ru.md --latex-engine=xelatex -o ru.pdf
pandoc doc/en.md --latex-engine=xelatex -o en.pdf
