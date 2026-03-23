---
name: security-compliance
description: Comprehensive security auditing, vulnerability scanning, and compliance checking. Use when performing security audits, scanning for vulnerabilities, checking code security, ensuring compliance with standards (SOC2, GDPR, ISO27001), implementing security best practices, or conducting penetration testing.
---

# Security & Compliance

Comprehensive security auditing and compliance checking.

## Overview

Ensure your applications and infrastructure are secure and compliant:
- 🔒 **Security Auditing** - Automated security scans
- 🐛 **Vulnerability Scanning** - Find and fix vulnerabilities
- 📋 **Compliance Checking** - Verify standards compliance
- 🔐 **Code Security** - Secure coding practices
- 🛡️ **Penetration Testing** - Ethical hacking assessments

## Security Scanning Tools

### Code Security

#### SAST (Static Application Security Testing)
```bash
# SonarQube
sonar-scanner \
  -Dsonar.projectKey=myproject \
  -Dsonar.sources=. \
  -Dsonar.host.url=https://sonarqube.example.com

# Semgrep
semgrep --config=auto .

# CodeQL (GitHub)
# Enabled via GitHub Advanced Security
```

#### Dependency Scanning
```bash
# npm audit
npm audit
npm audit fix

# Snyk
snyk test
snyk monitor

# OWASP Dependency Check
dependency-check.sh --project myproject --scan .
```

#### Secret Scanning
```bash
# GitLeaks
gitleaks detect --source . --verbose

# TruffleHog
trufflehog filesystem .

# GitHub Secret Scanning
# Enabled in repository settings
```

### Container Security

```bash
# Trivy
trivy image myapp:latest

# Clair
clair-scanner -c http://clair:6060 --ip scanner-ip myapp:latest

# Docker Bench
docker run -it --net host --pid host --userns host \
  --cap-add audit_control \
  -e DOCKER_CONTENT_TRUST=$DOCKER_CONTENT_TRUST \
  -v /var/lib:/var/lib \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -v /usr/lib/systemd:/usr/lib/systemd \
  -v /etc:/etc --label docker_bench_security \
  docker/docker-bench-security
```

### Infrastructure Security

```bash
# Checkov (IaC scanning)
checkov -d .

# tfsec (Terraform)
tfsec .

# CloudFormation Guard
cfn-guard validate -r rules.guard -d template.yaml
```

## Compliance Frameworks

### SOC 2
```yaml
# Trust Services Criteria
- Security: Protection against unauthorized access
- Availability: System availability for operation and monitoring
- Processing Integrity: Complete, valid, accurate, timely processing
- Confidentiality: Designated confidential information is protected
- Privacy: Personal information is collected, used, retained, and disclosed
```

### GDPR
```yaml
# Key Requirements
- Data minimization
- Purpose limitation
- Storage limitation
- Accuracy
- Integrity and confidentiality
- Accountability
- Data subject rights
```

### ISO 27001
```yaml
# Information Security Management
- Risk assessment and treatment
- Security policy
- Organization of information security
- Asset management
- Human resources security
- Physical and environmental security
- Communications and operations management
- Access control
- Information systems acquisition, development and maintenance
- Information security incident management
- Business continuity management
- Compliance
```

## Security Best Practices

### Secure Coding

#### Input Validation
```python
# Validate all inputs
def process_user_input(user_input):
    if not isinstance(user_input, str):
        raise ValueError("Input must be string")
    if len(user_input) > 1000:
        raise ValueError("Input too long")
    # Sanitize
    sanitized = html.escape(user_input)
    return sanitized
```

#### Authentication & Authorization
```python
# Use strong authentication
from functools import wraps
from flask import request, abort
import jwt

def require_auth(f):
    @wraps(f)
    def decorated(*args, **kwargs):
        token = request.headers.get('Authorization')
        if not token:
            abort(401)
        try:
            payload = jwt.decode(token, SECRET_KEY, algorithms=['HS256'])
            request.user = payload
        except jwt.InvalidTokenError:
            abort(401)
        return f(*args, **kwargs)
    return decorated
```

#### Secure Data Storage
```python
# Encrypt sensitive data
from cryptography.fernet import Fernet

key = Fernet.generate_key()
cipher = Fernet(key)

# Encrypt
encrypted = cipher.encrypt(b"sensitive data")

# Decrypt
decrypted = cipher.decrypt(encrypted)
```

### Security Checklist

#### Application Security
- [ ] Input validation and sanitization
- [ ] Output encoding
- [ ] Authentication and session management
- [ ] Access control
- [ ] CSRF protection
- [ ] XSS prevention
- [ ] SQL injection prevention
- [ ] Security headers
- [ ] HTTPS only
- [ ] Secure cookie settings

#### Infrastructure Security
- [ ] Network segmentation
- [ ] Firewall rules
- [ ] DDoS protection
- [ ] WAF (Web Application Firewall)
- [ ] Intrusion detection
- [ ] Log monitoring
- [ ] Backup and recovery
- [ ] Disaster recovery plan

#### Data Security
- [ ] Encryption at rest
- [ ] Encryption in transit
- [ ] Key management
- [ ] Data classification
- [ ] Data retention policy
- [ ] Secure data disposal
- [ ] Privacy by design

## Compliance Automation

### Automated Compliance Checks
```yaml
# Compliance as Code
compliance:
  soc2:
    - id: CC6.1
      description: Logical access security
      checks:
        - authentication_required
        - mfa_enabled
        - password_policy
    
  gdpr:
    - id: Article 25
      description: Data protection by design
      checks:
        - data_minimization
        - pseudonymization
        - encryption
```

### Compliance Reporting
```bash
# Generate compliance report
compliance-check --framework soc2 --output report.html

# Continuous compliance monitoring
compliance-watch --framework gdpr --notify slack
```

## Incident Response

### Incident Response Plan
```
1. Detection
   - Automated alerts
   - User reports
   - Log analysis

2. Containment
   - Isolate affected systems
   - Preserve evidence
   - Prevent further damage

3. Eradication
   - Remove threat
   - Patch vulnerabilities
   - Clean systems

4. Recovery
   - Restore from backups
   - Verify system integrity
   - Monitor for recurrence

5. Lessons Learned
   - Post-incident review
   - Update procedures
   - Improve defenses
```

## Security Monitoring

### SIEM (Security Information and Event Management)
```yaml
# Log sources
- Application logs
- System logs
- Network logs
- Cloud logs
- Security tool logs

# Alerts
- Failed login attempts
- Privilege escalation
- Data exfiltration
- Malware detection
- Anomalous behavior
```

### Threat Intelligence
```bash
# Subscribe to threat feeds
- MISP (Malware Information Sharing Platform)
- AlienVault OTX
- VirusTotal
- CISA Alerts
```

## Integration with Other Skills

- **cicd-pipeline** - Security in CI/CD
- **proactive-agent** - Security monitoring
- **writing-plans** - Security planning
- **skill-vetter** - Validate security configs

## Resources

- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [CIS Benchmarks](https://www.cisecurity.org/cis-benchmarks)
- [NIST Cybersecurity Framework](https://www.nist.gov/cyberframework)
- [SANS Security Resources](https://www.sans.org/security-resources/)
