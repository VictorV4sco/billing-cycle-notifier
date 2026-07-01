# Billing Cycle Notifier

Billing Cycle Notifier is a work-in-progress application for managing personal charges linked to specific people, grouping those charges by a configurable billing cycle, and sending automated WhatsApp payment reminders when a card statement closes.

The goal of the project is to help users keep track of shared expenses or personal debts in a structured way, with enough historical data to support future automation, message delivery, and payment tracking.

## Project Structure

This repository is organized as a simple monorepo:

- `backend/`: Spring Boot backend responsible for business rules, persistence, billing cycles, and future automation jobs
- `mobile/`: Expo / React Native mobile application

## Current Stack

### Backend

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- MySQL for development
- H2 for automated tests
- JaCoCo for test coverage reports

### Mobile

- Expo
- React Native
- TypeScript

## Current Domain Model

The backend currently includes the initial domain entities for the billing flow:

- `User`
- `Person`
- `Charge`
- `BillingStatement`
- `BillingStatementItem`
- `Message`

Status enums were also introduced for the entities that require lifecycle tracking:

- `ChargeStatus`
- `BillingStatementStatus`
- `MessageStatus`

The backend also includes an initial repository layer with custom JPQL queries for:

- `User`
- `Person`
- `Charge`
- `BillingStatement`
- `BillingStatementItem`
- `Message`

## Configuration

The backend already supports separated application profiles:

- `dev`: local development profile using MySQL
- `prod`: production profile using environment variables
- `test`: automated test profile using H2 in-memory database

Sensitive database credentials are not stored in versioned configuration files. Local development uses environment variables such as:

- `DB_USERNAME`
- `DB_PASSWORD`

## Testing

The project already includes an initial automated test base for the backend.

Current testing scope:

- context loading test
- JPA persistence tests for the core entities
- repository query tests for the current data access layer
- H2-based test profile for isolated database validation
- JaCoCo coverage report generation during test execution

Current tested areas:

- entity persistence and relationships
- default status values
- automatic timestamps from the base entity
- `generatedAt` initialization for billing statements
- unique constraint validation for billing statement cycles
- repository queries for users, people, charges, billing statements, statement items, and messages

## Planned Flow

The intended billing flow is:

1. A user registers people who may owe them money.
2. Charges are created and linked to a person.
3. The user defines a billing cycle interval and message sending time.
4. When the billing cycle closes, the backend will generate a billing statement for each person with pending charges in that period.
5. A message record will be created for that statement.
6. In a later stage, a scheduled job will automate the statement closing and WhatsApp message dispatch process.

## Current Status

This project is still in an early stage.

What is already in place:

- initial backend bootstrap
- mobile bootstrap with Expo
- environment-based backend configuration
- initial domain entity modeling
- status enums for core domain flows
- repository layer with custom JPQL queries
- initial automated persistence and repository tests
- JaCoCo coverage integration

What is expected next:

- service layer
- DTOs and validation rules
- REST endpoints
- statement generation logic
- scheduled job for cycle closing
- WhatsApp integration

## License

This project is distributed under the GNU General Public License v3.0. See the [LICENSE](LICENSE) file for details.
