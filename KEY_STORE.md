# Setting up the KEY_STORE secret in GitHub

CI needs a signing keystore to build c:geo. This isn't something a bot or automated
tool should set up for you — it requires repo admin access to Settings, and secrets
are sensitive — so here's how to do it yourself.

## 1. Get or generate a signing keystore

If you don't already have one, create one:

```bash
keytool -genkeypair -v -keystore key.keystore -alias cgeo -keyalg RSA -keysize 2048 -validity 10000
```

This prompts for a keystore password and a key (alias) password — remember both.

## 2. Base64-encode it

The workflow decodes it with `base64 -d`, so the secret must hold the base64 text,
not the raw binary:

```bash
base64 -w0 key.keystore > key.keystore.b64
```

(macOS: use `base64 -i key.keystore -o key.keystore.b64`, no `-w0` flag needed)

## 3. Add the secrets in GitHub

Go to your repo → **Settings → Secrets and variables → Actions → New repository
secret**, and add these four (all required by
`.github/actions/cgeo-preferences/action.yml`):

| Secret name | Value |
|---|---|
| `KEY_STORE` | contents of `key.keystore.b64` (the whole base64 blob) |
| `KEY_ALIAS` | the alias you used (`cgeo` in the example above) |
| `KEY_STORE_PASSWORD` | the keystore password |
| `KEY_ALIAS_PASSWORD` | the key/alias password |

## 4. Re-run CI

Once those four secrets exist, push a new commit (or re-run the failed workflow) —
the `check-secrets` job should pass and unblock the actual `packageBasicDebug` build
job, which is what produces a real compiled APK.

Note: this repo's workflows are also wired for several other optional secrets
(`MAPS_API_KEY`/`MAPS_API_KEY_MARKET` — already dead, unrelated to this app's current
map sources; `OCDE_OKAPI_CONSUMER_KEY` etc. for Opencaching connectors) that are
`required: false`, so you don't need those to unblock CI — only the four keystore
ones above are `required: true`.
